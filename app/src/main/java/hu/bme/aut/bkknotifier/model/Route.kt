package hu.bme.aut.bkknotifier.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Route (

  @SerializedName("id"              ) var id              : String?  = null,
  @SerializedName("shortName"       ) var shortName       : String?  = null,
  @SerializedName("description"     ) var description     : String?  = null,
  @SerializedName("type"            ) var type            : String?  = null,
  @SerializedName("color"           ) var color           : String?  = null,
  @SerializedName("textColor"       ) var textColor       : String?  = null,
  @SerializedName("agencyId"        ) var agencyId        : String?  = null,
  @SerializedName("iconDisplayType" ) var iconDisplayType : String?  = null,
  @SerializedName("iconDisplayText" ) var iconDisplayText : String?  = null,
  @SerializedName("bikesAllowed"    ) var bikesAllowed    : Boolean? = null,
  @SerializedName("style"           ) var style           : Style?   = Style(),
  @SerializedName("sortOrder"       ) var sortOrder       : Int?     = null

) : Serializable