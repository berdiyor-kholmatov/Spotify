package com.example.spotify.data.mapper

import com.example.spotify.data.db.MusicFileEntity
import com.example.spotify.domain.model.MusicFile
import com.example.spotify.domain.util.Mapper
import javax.inject.Inject
import kotlin.Long

class DataMapper @Inject constructor(): Mapper<MusicFileEntity, MusicFile> {
    override fun domainToModel(domain: MusicFile): MusicFileEntity {
        return MusicFileEntity(
            id = domain.id ?: 0,
            name = domain.name,
            artist = domain.artist,
            duration = domain.duration,
            filePath = domain.filePath,
            albumId = domain.albumId
        )
    }

    override fun modelToDomain(model: MusicFileEntity): MusicFile {
        return MusicFile(
            id = model.id,
            name = model.name,
            artist = model.artist,
            duration = model.duration,
            filePath = model.filePath,
            albumId = model.albumId
        )
    }

}
