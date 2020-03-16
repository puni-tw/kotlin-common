package puni.data.entity

import org.springframework.web.bind.annotation.RequestMethod
import puni.zygarde.api.ApiProp
import puni.zygarde.api.Dto
import puni.zygarde.api.GenApi
import puni.zygarde.api.RequestDto
import puni.zygarde.api.ZygardeApi
import javax.persistence.Entity
import javax.persistence.Transient

private const val UserApi = "UserApi"
private const val PhoneService = "PhoneService"
private const val REQ_SEND_VALIDATE_PHONE = "SendPhoneValidationRequest"
private const val RES_SEND_VALIDATE_PHONE = "PhoneValidation"
private const val REQ_DO_VALIDATE_PHONE = "ValidatePhoneRequest"

@ZygardeApi(
  api = [
    GenApi(
      method = RequestMethod.POST,
      path = "/p/api/phoneValidation",
      pathVariable = [],
      apiName = UserApi,
      apiOperation = "sendPhoneValidation",
      apiDescription = "發送簡訊驗證碼",
      serviceName = PhoneService,
      serviceMethod = "sendPhoneValidation",
      reqRef = REQ_SEND_VALIDATE_PHONE,
      resRef = RES_SEND_VALIDATE_PHONE
    ),
    GenApi(
      method = RequestMethod.PUT,
      path = "/p/api/phoneValidation",
      pathVariable = [],
      apiName = UserApi,
      apiOperation = "doValidatePhone",
      apiDescription = "驗證手機驗證碼",
      serviceName = PhoneService,
      serviceMethod = "doValidatePhone",
      reqRef = REQ_DO_VALIDATE_PHONE,
      resRef = ""
    )
  ]
)
@Entity
class User(
  @ApiProp(
    comment = "手機號碼",
    requestDto = [
      RequestDto(REQ_SEND_VALIDATE_PHONE, applyValueToEntity = false)
    ]
  )
  val phone: String = "",
  val pwd: String = ""
) : AutoIdEntity() {

  @Transient
  @ApiProp(
    comment = "驗證Token",
    dto = [
      Dto(RES_SEND_VALIDATE_PHONE, applyValueFromEntity = false)
    ],
    requestDto = [
      RequestDto(REQ_DO_VALIDATE_PHONE, applyValueToEntity = false)
    ]
  )
  val phoneValidationToken: String = ""

  @Transient
  @ApiProp(
    comment = "驗證碼",
    requestDto = [
      RequestDto(REQ_DO_VALIDATE_PHONE, applyValueToEntity = false)
    ]
  )
  val phoneValidationCode: String = ""
}
