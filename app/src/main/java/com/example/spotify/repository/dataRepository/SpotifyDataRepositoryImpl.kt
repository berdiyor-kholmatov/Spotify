package com.example.spotify.repository.dataRepository

import com.example.spotify.data.db.SpotifyDao
import com.example.spotify.data.mapper.DataMapper
import com.example.spotify.domain.model.MusicFile
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SpotifyDataRepositoryImpl @Inject constructor(
    private val spotifyDao: SpotifyDao,
    private val mapper: DataMapper
) : SpotifyDataRepository {

    /**
     * Observes the collection of music files from the local database.
     *
     * This method provides a continuous stream of data, emitting an updated list of [MusicFile]
     * domain models whenever the underlying database entities are modified.
     *
     * @return A [Flow] emitting a list of [MusicFile] objects.
     */
    override fun observeMusicFiles(): Flow<List<MusicFile>> {
        return spotifyDao.observeMusicFiles().map { entities ->
            entities.map { entity ->
                mapper.modelToDomain(entity)
            }
        }
    }

    /**
     * Inserts a list of music files into the repository.
     *
     * This method maps the provided domain [MusicFile] objects to their corresponding
     * database entities and persists them using the data access object.
     *
     * @param musicFiles The list of domain music files to be inserted.
     */
    override suspend fun insert(musicFiles: List<MusicFile>) {
        val listOfMusicFileEntities = musicFiles.map { musicFile ->
            mapper.domainToModel(musicFile)
        }
        spotifyDao.insert(listOfMusicFileEntities)
    }

    /**
     * Deletes a specific music file from the repository based on its unique identifier.
     *
     * @param id The unique identifier of the music file to be deleted.
     */
    override suspend fun delete(id: Long) {
        spotifyDao.delete(id)
    }
}
