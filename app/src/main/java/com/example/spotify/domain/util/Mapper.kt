package com.example.spotify.domain.util

interface Mapper<M, D> {
    fun domainToModel(domain: D): M
    fun modelToDomain(model: M): D
}