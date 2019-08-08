/*
 *   Copyright 1999-2018 dragonshard.net.
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package net.dragonshard.dsf.data.secret.advice;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import net.dragonshard.dsf.data.secret.algorithm.AbstractRequestDecrypt;
import net.dragonshard.dsf.data.secret.algorithm.RequestAESDecrypt;
import net.dragonshard.dsf.data.secret.algorithm.RequestRSADecrypt;
import net.dragonshard.dsf.data.secret.algorithm.key.AESKey;
import net.dragonshard.dsf.data.secret.algorithm.key.RSAKey;
import net.dragonshard.dsf.data.secret.annotation.SecretBody;
import net.dragonshard.dsf.data.secret.configuration.property.AESProperties;
import net.dragonshard.dsf.data.secret.configuration.property.RSAProperties;
import net.dragonshard.dsf.data.secret.exception.SecretRequestDecryptException;
import net.dragonshard.dsf.data.secret.model.SecretHttpMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.security.Provider;
import java.util.Set;

import static net.dragonshard.dsf.web.core.common.WebCoreConstants.SECRET.ALGORITHM_AES;
import static net.dragonshard.dsf.web.core.common.WebCoreConstants.SECRET.ALGORITHM_RSA;
import static net.dragonshard.dsf.web.core.enums.DsfErrorCodeEnum.SECRET_DECRYPT_BODY_FAIL;


/**
 * 执行于convertMessage之前
 *
 * @author mayee
 * @version v1.0
 * @date 2019-06-25
 **/
@Slf4j
@ControllerAdvice
@Order(1)
@ConditionalOnProperty(prefix = "dragonshard.secret", name = "enabled", matchIfMissing = true)
public class SecretRequestAdvice extends RequestBodyAdviceAdapter {
    @Autowired
    private AESProperties aesProperties;
    @Autowired
    private RSAProperties rsaProperties;

    // 支持的加密算法
    private final Set<String> ALGORITHM_SET = Sets.newHashSet(
            ALGORITHM_AES,
            ALGORITHM_RSA
    );

    /**
     * 是否支持加密消息体
     *
     * @param methodParameter methodParameter
     * @return true/false
     */
    private boolean supportSecretRequest(MethodParameter methodParameter) {
        Class currentClass = methodParameter.getContainingClass();

        //类注解
        Annotation classAnnotation = currentClass.getAnnotation(SecretBody.class);
        //方法注解
        SecretBody methodAnnotation = methodParameter.getMethodAnnotation(SecretBody.class);
        //如果类与方法均不存在注解，则排除
        if (classAnnotation == null && methodAnnotation == null) {
            return false;
        }

        boolean classAnnotationCheck = classAnnotation != null
                && (
                // 含有排除标识
                ((SecretBody) classAnnotation).exclude()
                        // 或者是不支持的加密类型
                        || notSupportAlgorithm(((SecretBody) classAnnotation).value()));
        if (classAnnotationCheck) {
            return false;
        }

        boolean methodAnnotationCheck = methodAnnotation != null
                && (
                // 含有排除标识
                methodAnnotation.exclude()
                        // 或者是不支持的加密类型
                        || notSupportAlgorithm(methodAnnotation.value()));
        if (methodAnnotationCheck) {
            return false;
        }

        return true;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return supportSecretRequest(methodParameter);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        String httpBody = decryptBody(inputMessage, parameter);
        if (httpBody == null) {
            throw new SecretRequestDecryptException(SECRET_DECRYPT_BODY_FAIL.msg());
        }
        return new SecretHttpMessage(new ByteArrayInputStream(httpBody.getBytes()), inputMessage.getHeaders());
    }

    /**
     * 解密消息体
     *
     * @param inputMessage 消息体
     * @return 明文
     */
    private String decryptBody(HttpInputMessage inputMessage, MethodParameter parameter) {
        try {
            InputStream encryptStream = inputMessage.getBody();
            String encryptBody = StreamUtils.copyToString(encryptStream, Charset.defaultCharset());

            if (log.isDebugEnabled()) {
                log.debug("Decrypt body for input > {}", encryptBody);
            }

            SecretBody secretBodyAnnotation = (SecretBody) getAlgorithmValue(parameter);
            if(secretBodyAnnotation != null){
                String algorithmName = secretBodyAnnotation.value();
                String ciphertextType = secretBodyAnnotation.ciphertextType();
                if(ALGORITHM_AES.equalsIgnoreCase(algorithmName)){
                    AESKey aesKey = new AESKey(aesProperties.getKey());
                    AbstractRequestDecrypt requestDecrypt = new RequestAESDecrypt(ciphertextType, aesKey);
                    return requestDecrypt.decryptBody(encryptBody);
                }else if(ALGORITHM_RSA.equalsIgnoreCase(algorithmName)){
                    RSAKey rsaKey = new RSAKey(
                            rsaProperties.getPublicKey(),
                            rsaProperties.getPrivateKey(),
                            rsaProperties.getModulus(),
                            null
                    );
                    Class<? extends Provider>[] providerClass = secretBodyAnnotation.providerClass();
                    if(providerClass.length > 0){
                        rsaKey.setProviderClass(providerClass[0]);
                    }
                    AbstractRequestDecrypt requestDecrypt = new RequestRSADecrypt(ciphertextType, rsaKey);
                    return requestDecrypt.decryptBody(encryptBody);
                }
            }else{
                return encryptBody;
            }

        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return null;
    }

    private SecretBody getAlgorithmValue(MethodParameter parameter){
        // 先找方法注解
        SecretBody methodAnnotation = parameter.getMethodAnnotation(SecretBody.class);
        if(methodAnnotation != null && !methodAnnotation.exclude()){
            return methodAnnotation;
        }
        Class currentClass = parameter.getContainingClass();
        Annotation classAnnotation = currentClass.getAnnotation(SecretBody.class);
        if(classAnnotation != null){
            return (SecretBody) classAnnotation;
        }
        return null;
    }

    private boolean notSupportAlgorithm(String algorithmName){
        return !ALGORITHM_SET.contains(algorithmName.toUpperCase());
    }

}
