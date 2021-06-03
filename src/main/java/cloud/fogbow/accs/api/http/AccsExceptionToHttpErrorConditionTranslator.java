package cloud.fogbow.accs.api.http;

import org.springframework.web.bind.annotation.ControllerAdvice;

import cloud.fogbow.common.http.FogbowExceptionToHttpErrorConditionTranslator;

@ControllerAdvice
public class AccsExceptionToHttpErrorConditionTranslator extends FogbowExceptionToHttpErrorConditionTranslator {
}
