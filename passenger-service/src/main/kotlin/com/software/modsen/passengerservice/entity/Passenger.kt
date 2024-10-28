package com.software.modsen.passengerservice.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "passengers")
class Passenger {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:Column(name = "passenger_id")
    var id: Long?

    @field:Column(name = "firstname")
    var firstname: String

    @field:Column(name = "surname")
    var surname: String

    @field:Column(name = "patronymic")
    var patronymic: String

    @field:Column(name = "email")
    var email: String

    @field:Column(name = "phone_number")
    var phoneNumber: String

    @field:Column(name = "is_restricted")
    var restricted: Boolean

    @field:OneToOne(mappedBy = "passenger", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var passengerRating: PassengerRating?

    constructor(){
        id = null
        firstname = ""
        surname = ""
        patronymic = ""
        email = ""
        phoneNumber = ""
        restricted = false
        passengerRating = null
    }

    constructor(id: Long?) : this() {
        this.id = id
    }

    constructor(
        id: Long?,
        firstname: String,
        surname: String,
        patronymic: String,
        email: String,
        phoneNumber: String,
        restricted: Boolean,
        passengerRating: PassengerRating?
    ) : this() {
        this.id = id
        this.firstname = firstname
        this.surname = surname
        this.patronymic = patronymic
        this.email = email
        this.phoneNumber = phoneNumber
        this.restricted = restricted
        this.passengerRating = passengerRating
    }
}