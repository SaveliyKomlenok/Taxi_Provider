package com.software.modsen.passengerservice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.Version

@Entity
@Table(name = "passenger_ratings")
class PassengerRating {
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    @field:Column(name= "passenger_rating_id")
    var id: Long?

    @field:Column(name = "rating")
    var rating: Double

    @field:OneToOne(fetch = FetchType.EAGER)
    @field:JoinColumn(name = "passenger_id")
    var passenger: Passenger?

    @field:Column(name = "version")
    @field:Version
    private val version: Long?

    constructor() {
        id = null
        rating = 0.0
        passenger = null
        version = null
    }

    constructor(rating: Double, passenger: Passenger?) : this() {
        this.rating = rating
        this.passenger = passenger
    }

    constructor(id: Long?, rating: Double, passenger: Passenger?) : this() {
        this.id = id
        this.rating = rating
        this.passenger = passenger
    }
}