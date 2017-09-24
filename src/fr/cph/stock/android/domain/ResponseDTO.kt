package fr.cph.stock.android.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import kotlin.properties.Delegates

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
class ResponseDTO {
    var portfolio: Portfolio by Delegates.notNull()

    var error: String? = null
}
