package com.pubnub.components.example.getting_started.dto

import com.google.gson.annotations.SerializedName

data class Doctors(

	@field:SerializedName("Doctors")
	val doctors: List<DoctorsItem?>? = null
)

data class DoctorsItem(

	@field:SerializedName("profileUrl")
	val profileUrl: String? = null,

	@field:SerializedName("custom")
	val custom: CustomDoctors? = null,

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

data class CustomDoctors(

	@field:SerializedName("title")
	val title: String? = null
)
