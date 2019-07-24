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
package net.dragonshard.dsf.web.core.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.common.base.Throwables;
import net.dragonshard.dsf.web.core.enums.DsfErrorCodeEnum;
import net.dragonshard.dsf.web.core.framework.exception.BizException;
import net.dragonshard.dsf.web.core.framework.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

/**
 * 全程异常处理
 * {@link org.springframework.web.servlet.HandlerExceptionResolver
 * {@link org.springframework.web.servlet.DispatcherServlet}.
 *
 * @author Caratacus
 * @author Mayee
 * @see org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
 * @see #handleNoSuchRequestHandlingMethod
 * @see #handleHttpRequestMethodNotSupported
 * @see #handleHttpMediaTypeNotSupported
 * @see #handleMissingServletRequestParameter
 * @see #handleServletRequestBindingException
 * @see #handleTypeMismatch
 * @see #handleHttpMessageNotReadable
 * @see #handleHttpMessageNotWritable
 * @see #handleMethodArgumentNotValidException
 * @see #handleMissingServletRequestParameter
 * @see #handleMissingServletRequestPartException
 * @see #handleBindException
 * @see org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver
 */
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class WebHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

    private static final ModelAndView MODEL_VIEW_INSTANCE = new ModelAndView();

    @Override
    protected ModelAndView doResolveException(@NonNull HttpServletRequest request,
                                              @NonNull HttpServletResponse response,
                                              Object handler,
                                              @NonNull Exception ex) {
        try {
            if (ex instanceof BizException) {
                handleBiz((BizException) ex, request, response);
            } else if (ex instanceof HttpRequestMethodNotSupportedException) {
                handleHttpRequestMethodNotSupported((HttpRequestMethodNotSupportedException) ex, request, response);
            } else if (ex instanceof HttpMediaTypeNotSupportedException) {
                handleHttpMediaTypeNotSupported((HttpMediaTypeNotSupportedException) ex, request, response);
            } else if (ex instanceof HttpMediaTypeNotAcceptableException) {
                handleHttpMediaTypeNotAcceptable((HttpMediaTypeNotAcceptableException) ex, request, response);
            } else if (ex instanceof MissingPathVariableException) {
                handleMissingPathVariable((MissingPathVariableException) ex, request, response);
            } else if (ex instanceof MissingServletRequestParameterException) {
                handleMissingServletRequestParameter((MissingServletRequestParameterException) ex, request, response);
            } else if (ex instanceof ServletRequestBindingException) {
                handleServletRequestBindingException((ServletRequestBindingException) ex, request, response);
            } else if (ex instanceof ConversionNotSupportedException) {
                handleConversionNotSupported((ConversionNotSupportedException) ex, request, response);
            } else if (ex instanceof TypeMismatchException) {
                handleTypeMismatch((TypeMismatchException) ex, request, response);
            } else if (ex instanceof HttpMessageNotReadableException) {
                handleHttpMessageNotReadable((HttpMessageNotReadableException) ex, request, response);
            } else if (ex instanceof HttpMessageNotWritableException) {
                handleHttpMessageNotWritable((HttpMessageNotWritableException) ex, request, response);
            } else if (ex instanceof MethodArgumentNotValidException) {
                handleMethodArgumentNotValidException((MethodArgumentNotValidException) ex, request, response);
            } else if (ex instanceof MissingServletRequestPartException) {
                handleMissingServletRequestPartException((MissingServletRequestPartException) ex, request, response);
            } else if (ex instanceof BindException) {
                handleBindException((BindException) ex, request, response);
            } else if (ex instanceof NoHandlerFoundException) {
                handleNoHandlerFoundException((NoHandlerFoundException) ex, request, response);
            } else if (ex instanceof AsyncRequestTimeoutException) {
                handleAsyncRequestTimeoutException((AsyncRequestTimeoutException) ex, request, response);
            } else if (ex instanceof ConstraintViolationException) {
                handleConstraintViolationException((ConstraintViolationException) ex, request, response);
            }  else {
                handleException(ex, request, response);
            }
        } catch (Exception handlerException) {
            if (log.isWarnEnabled()) {
                log.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", handlerException);
            }
        }
        if (response.getStatus() < HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            log.info("Info: doResolveInfo {}", ex.getMessage());
        } else {
            log.warn("Warn: doResolveException {}", Throwables.getStackTraceAsString(ex));
        }
        return MODEL_VIEW_INSTANCE;
    }

    /**
     * Handle the case where exception
     *
     * @param ex       the ApiException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleBiz(BizException ex,
                             HttpServletRequest request, HttpServletResponse response) {
        ResponseUtils.sendFail(request, response, ex.getErrorCode());

    }

    /**
     * Handle the case where no request handler method was found for the
     * particular HTTP request method.
     * <p>
     * The default implementation logs a warning, sends an HTTP 405 error, sets
     * the "Allow" header,
     * Alternatively, a fallback view could be chosen, or the
     * HttpRequestMethodNotSupportedException could be rethrown as-is.
     *
     * @param ex       the HttpRequestMethodNotSupportedException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                       HttpServletRequest request, HttpServletResponse response) {
        ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.METHOD_NOT_ALLOWED.convert(), ex);
    }

    /**
     * Handle the case where no
     * {@linkplain org.springframework.http.converter.HttpMessageConverter
     * message converters} were found for the PUT or POSTed content.
     * <p>
     * The default implementation sends an HTTP 415 error, sets the "Accept"
     * header,  Alternatively, a
     * fallback view could be chosen, or the HttpMediaTypeNotSupportedException
     * could be rethrown as-is.
     *
     * @param ex       the HttpMediaTypeNotSupportedException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest request,
                                                   HttpServletResponse response) {
        ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.UNSUPPORTED_MEDIA_TYPE.convert(), ex);
    }

    /**
     * Handle the case where no
     * {@linkplain org.springframework.http.converter.HttpMessageConverter
     * message converters} were found that were acceptable for the client
     * (expressed via the {@code Accept} header.
     * <p>
     * The default implementation sends an HTTP 406 error and returns an empty
     * {@code ModelAndView}. Alternatively, a fallback view could be chosen, or
     * the HttpMediaTypeNotAcceptableException could be rethrown as-is.
     *
     * @param ex       the HttpMediaTypeNotAcceptableException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpServletRequest request,
                                                    HttpServletResponse response) {
        ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.NOT_ACCEPTABLE.convert(), ex);
    }

    /**
     * Handle the case when a declared path variable does not match any
     * extracted URI variable.
     * <p>
     * The default implementation sends an HTTP 500 error, and returns an empty
     * {@code ModelAndView}. Alternatively, a fallback view could be chosen, or
     * the MissingPathVariableException could be rethrown as-is.
     *
     * @param ex       the MissingPathVariableException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleMissingPathVariable(MissingPathVariableException ex, HttpServletRequest request,
                                             HttpServletResponse response) {
        ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.INTERNAL_SERVER_ERROR.convert(), ex);
    }

    /**
     * Handle the case when a required parameter is missing.
     * <p>
     * The default implementation sends an HTTP 400 error, and returns an empty
     * {@code ModelAndView}. Alternatively, a fallback view could be chosen, or
     * the MissingServletRequestParameterException could be rethrown as-is.
     *
     * @param ex       the MissingServletRequestParameterException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                        HttpServletRequest request, HttpServletResponse response) {
        ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.BAD_REQUEST.convert(), ex);
    }

    /**
     * Handle the case when an unrecoverable binding exception occurs - e.g.
     * required header, required cookie.
     * <p>
     * The default implementation sends an HTTP 400 error, and returns an empty
     * {@code ModelAndView}. Alternatively, a fallback view could be chosen, or
     * the exception could be rethrown as-is.
     *
     * @param ex       the exception to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleServletRequestBindingException(ServletRequestBindingException ex, HttpServletRequest request,
                                                        HttpServletResponse response) {
        ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.BAD_REQUEST.convert(), ex);
    }

    /**
     * Handle the case when a {@link org.springframework.web.bind.WebDataBinder}
     * conversion cannot occur.
     * <p>
     * The default implementation sends an HTTP 500 error, and returns an empty
     * {@code ModelAndView}. Alternatively, a fallback view could be chosen, or
     * the TypeMismatchException could be rethrown as-is.
     *
     * @param ex       the ConversionNotSupportedException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleConversionNotSupported(ConversionNotSupportedException ex, HttpServletRequest request,
                                                HttpServletResponse response) {
        sendServerError(ex, request, response);
    }

    /**
     * Handle the case when a {@link org.springframework.web.bind.WebDataBinder}
     * conversion error occurs.
     * <p>
     * The default implementation sends an HTTP 400 error, and returns an empty
     * {@code ModelAndView}. Alternatively, a fallback view could be chosen, or
     * the TypeMismatchException could be rethrown as-is.
     *
     * @param ex       the TypeMismatchException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleTypeMismatch(TypeMismatchException ex, HttpServletRequest request, HttpServletResponse response) {
        ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.BAD_REQUEST.convert(), ex);
    }

    /**
     * Handle the case where a
     * {@linkplain org.springframework.http.converter.HttpMessageConverter
     * message converter} cannot read from a HTTP request.
     * <p>
     * The default implementation sends an HTTP 400 error, and returns an empty
     * {@code ModelAndView}. Alternatively, a fallback view could be chosen, or
     * the HttpMediaTypeNotSupportedException could be rethrown as-is.
     *
     * @param ex       the HttpMessageNotReadableException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request,
                                                HttpServletResponse response) {
        if (ex.getCause() instanceof JsonParseException) {
            ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.JSON_FORMAT_ERROR.convert(), ex);
        } else {
            ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.BAD_REQUEST.convert(), ex);
        }
    }

    /**
     * Handle the case where a
     * {@linkplain org.springframework.http.converter.HttpMessageConverter
     * message converter} cannot write to a HTTP request.
     * <p>
     * The default implementation sends an HTTP 500 error, and returns an empty
     * {@code ModelAndView}. Alternatively, a fallback view could be chosen, or
     * the HttpMediaTypeNotSupportedException could be rethrown as-is.
     *
     * @param ex       the HttpMessageNotWritableException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpServletRequest request,
                                                HttpServletResponse response) {
        sendServerError(ex, request, response);
    }

    /**
     * Handle the case where an argument annotated with {@code @Valid} such as
     * an {@link RequestBody} or {@link RequestPart} argument fails validation.
     * An HTTP 400 error is sent back to the client.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request,
                                                         HttpServletResponse response) {
        ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.BAD_REQUEST.convert(), ex);
    }

    /**
     * Handle the case where an {@linkplain RequestPart @RequestPart}, a
     * {@link MultipartFile}, or a {@code javax.servlet.http.Part} argument is
     * required but is missing. An HTTP 400 error is sent back to the client.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleMissingServletRequestPartException(MissingServletRequestPartException ex,
                                                            HttpServletRequest request, HttpServletResponse response) {
        ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.BAD_REQUEST.convert(), ex);
    }

    /**
     * Handle the case where an {@linkplain ModelAttribute @ModelAttribute}
     * method argument has binding or validation errors and is not followed by
     * another method argument of type {@link BindingResult}. By default, an
     * HTTP 400 error is sent back to the client.
     *
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleBindException(BindException ex, HttpServletRequest request, HttpServletResponse response) {
        ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.BAD_REQUEST.convert(), ex);
    }

    /**
     * Handle the case where no handler was found during the dispatch.
     * <p>
     * The default implementation sends an HTTP 404 error and returns an empty
     * {@code ModelAndView}. Alternatively, a fallback view could be chosen, or
     * the NoHandlerFoundException could be rethrown as-is.
     *
     * @param ex       the NoHandlerFoundException to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request,
                                                 HttpServletResponse response) {
        ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.NOT_FOUND.convert(), ex);
    }

    /**
     * Handle the case where an async request timed out.
     * <p>
     * The default implementation sends an HTTP 503 error.
     *
     * @param ex       the {@link AsyncRequestTimeoutException }to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex, HttpServletRequest request,
                                                      HttpServletResponse response) {
        ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.SERVICE_UNAVAILABLE.convert(), ex);
    }

    /**
     * Handle the case where an async request timed out.
     * <p>
     * The default implementation sends an HTTP 400 error.
     *
     * @param ex       the {@link ConstraintViolationException }to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request,
                                                      HttpServletResponse response) {
        ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.BAD_REQUEST.convert(), ex);
    }

    /**
     * Handle the case where an other error.
     * <p>
     * The default implementation sends an HTTP 500 error.
     *
     * @param ex       the {@link Exception }to be handled
     * @param request  current HTTP request
     * @param response current HTTP response
     */
    private void handleException(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.INTERNAL_SERVER_ERROR.convert(), ex);
    }

    /**
     * Invoked to send a server error. Sets the status to 500 and also sets the
     * request attribute "javax.servlet.error.exception" to the Exception.
     */
    private void sendServerError(Exception ex, HttpServletRequest request, HttpServletResponse response) {
        ResponseUtils.sendFail(request, response, DsfErrorCodeEnum.INTERNAL_SERVER_ERROR.convert(), ex);
    }

}
