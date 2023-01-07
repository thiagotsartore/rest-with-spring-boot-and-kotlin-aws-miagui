package br.com.miagui.data.vo.v1

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("Id", "Address", "First_Name", "Last_Name", "Gender")
data class PersonVO (

    @field:JsonProperty("Id")
    var id: Long = 0,

    @field:JsonProperty("First_Name")
    var firstName: String = "",

    @field:JsonProperty("Last_Name")
    var lastName: String = "",

    @field:JsonProperty("Address")
    var address: String = "",

    @field:JsonIgnore
    var gender: String = ""
)