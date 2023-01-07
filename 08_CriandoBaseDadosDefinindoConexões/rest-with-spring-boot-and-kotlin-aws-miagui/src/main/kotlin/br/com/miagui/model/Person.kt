package br.com.miagui.model

import jakarta.persistence.*

@Entity
@Table(name = "person")
data class Person (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(name = "first_name", nullable = false, length = 20)
    var firstName: String = "",

    @Column(name = "last_name", nullable = false, length = 20)
    var lastName: String = "",

    @Column(nullable = false, length = 50)
    var address: String = "",

    @Column(nullable = false, length = 15)
    var gender: String = ""
)