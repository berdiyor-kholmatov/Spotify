package com.example.spotify.data.mapper

import com.example.spotify.data.db.MusicFileEntity
import com.example.spotify.domain.model.MusicFile
import com.example.spotify.domain.util.Mapper
import kotlin.Long

class DataMapper: Mapper<MusicFileEntity, MusicFile> {
    override fun domainToModel(domain: MusicFile): MusicFileEntity {
        return MusicFileEntity(
            id = domain.id,
            name = domain.name,
            duration = domain.duration,
            filePath = domain.filePath
        )
    }

    override fun modelToDomain(model: MusicFileEntity): MusicFile {
        return MusicFile(
            id = model.id,
            name = model.name,
            duration = model.duration,
            filePath = model.filePath
        )
    }

}