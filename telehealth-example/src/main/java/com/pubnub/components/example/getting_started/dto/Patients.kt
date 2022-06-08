package com.pubnub.components.example.getting_started.dto

import com.google.gson.annotations.SerializedName

data class Patients(

	@field:SerializedName("Patients")
	val patients: List<PatientsItem?>? = null
)

data class PatientsItem(

	@field:SerializedName("profileUrl")
	val profileUrl: String? = null,

	@field:SerializedName("custom")
	val custom: CustomPatient? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("externalId")
	val externalId: Any? = null,

	@field:SerializedName("eTag")
	val eTag: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("updated")
	val updated: String? = null,

	@field:SerializedName("email")
	val email: Any? = null
)

data class CustomPatient(

	@field:SerializedName("title")
	val title: String? = null
)
