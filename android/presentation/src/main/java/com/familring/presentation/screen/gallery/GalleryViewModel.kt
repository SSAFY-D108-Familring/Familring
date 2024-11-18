package com.familring.presentation.screen.gallery

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.familring.domain.datastore.TutorialDataStore
import com.familring.domain.model.ApiResponse
import com.familring.domain.model.gallery.AlbumType
import com.familring.domain.repository.GalleryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel
    @Inject
    constructor(
        private val galleryRepository: GalleryRepository,
        private val tutorialDataStore: TutorialDataStore,
        private val context: Context,
    ) : ViewModel() {
        private val _tutorialUiState = MutableStateFlow(TutorialUiState())
        val tutorialUiState = _tutorialUiState.asStateFlow()

        private val _galleryUiState = MutableStateFlow<GalleryUiState>(GalleryUiState.Loading)
        val galleryUiState = _galleryUiState.asStateFlow()

        private val _galleryUiEvent = MutableSharedFlow<GalleryUiEvent>()
        val galleryUiEvent = _galleryUiEvent.asSharedFlow()

        private val _photoUiState = MutableStateFlow<PhotoUiState>(PhotoUiState.Loading)
        val photoUiState = _photoUiState.asStateFlow()

        private var currentAlbumTypes: List<AlbumType> =
            listOf(AlbumType.NORMAL, AlbumType.SCHEDULE, AlbumType.PERSON)

        init {
            getReadTutorial()
        }

        private fun getReadTutorial() {
            viewModelScope.launch {
                _tutorialUiState.update {
                    it.copy(
                        isReadTutorial = tutorialDataStore.getAlbumReadTutorial(),
                    )
                }
            }
        }

        fun setReadTutorial() {
            viewModelScope.launch {
                tutorialDataStore.setAlbumReadTutorial(true)
                _tutorialUiState.update {
                    it.copy(
                        isReadTutorial = true,
                    )
                }
            }
        }

        fun setReadTutorialState(isRead: Boolean) {
            viewModelScope.launch {
                _tutorialUiState.update {
                    it.copy(
                        isReadTutorial = isRead,
                    )
                }
            }
        }

        fun getAlbums(albumTypes: List<AlbumType>) {
            currentAlbumTypes = albumTypes
            loadAlbums()
        }

        private fun loadAlbums() {
            viewModelScope.launch {
                try {
                    val albumTypesString = currentAlbumTypes.joinToString(",") { it.name }
                    galleryRepository.getAlbums(albumTypesString).collectLatest { response ->
                        when (response) {
                            is ApiResponse.Success -> {
                                _galleryUiState.value =
                                    GalleryUiState.Success(
                                        normalAlbums =
                                            (
                                                response.data.NORMAL?.toMutableList() ?: emptyList()
                                            ) + (
                                                response.data.SCHEDULE?.toMutableList() ?: emptyList()
                                            ),
                                        personAlbums = response.data.PERSON ?: emptyList(),
                                    )
                            }

                            is ApiResponse.Error -> {
                                _galleryUiState.value =
                                    GalleryUiState.Error(
                                        errorMessage = response.message,
                                    )
                            }
                        }
                    }
                } catch (e: Exception) {
                    _galleryUiState.value =
                        GalleryUiState.Error(
                            errorMessage = e.message ?: "Unknown error occurred",
                        )
                }
            }
        }

        fun createAlbum(
            scheduleId: Long?,
            albumName: String,
            albumType: AlbumType,
        ) {
            viewModelScope.launch {
                galleryRepository
                    .createAlbum(scheduleId, albumName, albumType.name)
                    .collectLatest { response ->
                        when (response) {
                            is ApiResponse.Success -> {
                                _galleryUiEvent.emit(GalleryUiEvent.Success)
                                loadAlbums()
                            }

                            is ApiResponse.Error -> {
                                _galleryUiEvent.emit(
                                    GalleryUiEvent.Error(
                                        response.code,
                                        response.message,
                                    ),
                                )
                                Timber.d("code: ${response.code}, message: ${response.message}")
                            }
                        }
                    }
            }
        }

        fun getOneAlbum(albumId: Long) {
            viewModelScope.launch {
                galleryRepository.getOneAlbum(albumId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _photoUiState.value =
                                PhotoUiState.Success(
                                    albumName = response.data.albumName,
                                    photoList = response.data.photos,
                                )
                        }

                        is ApiResponse.Error -> {
                            _photoUiState.value =
                                PhotoUiState.Error(
                                    errorMessage = response.message,
                                )
                            _galleryUiEvent.emit(
                                GalleryUiEvent.Error(
                                    response.code,
                                    response.message,
                                ),
                            )
                        }
                    }
                }
            }
        }

        fun updateAlbum(
            albumId: Long,
            albumName: String,
        ) {
            viewModelScope.launch {
                galleryRepository.updateAlbum(albumId, albumName).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _galleryUiEvent.emit(GalleryUiEvent.Success)
                            loadAlbums()
                        }

                        is ApiResponse.Error -> {
                            _galleryUiEvent.emit(
                                GalleryUiEvent.Error(
                                    response.code,
                                    response.message,
                                ),
                            )
                        }
                    }
                }
            }
        }

        fun deleteAlbum(albumId: Long) {
            viewModelScope.launch {
                galleryRepository.deleteAlbum(albumId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _galleryUiEvent.emit(GalleryUiEvent.Success)
                            loadAlbums()
                        }

                        is ApiResponse.Error -> {
                            _galleryUiEvent.emit(
                                GalleryUiEvent.Error(
                                    response.code,
                                    response.message,
                                ),
                            )
                        }
                    }
                }
            }
        }

        fun uploadPhotos(
            albumId: Long,
            photos: List<File>,
        ) {
            viewModelScope.launch {
                _galleryUiEvent.emit(GalleryUiEvent.Loading)
                galleryRepository.uploadPhotos(albumId, photos).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _galleryUiEvent.emit(GalleryUiEvent.Success)
                            getOneAlbum(albumId)
                        }

                        is ApiResponse.Error -> {
                            _galleryUiEvent.emit(
                                GalleryUiEvent.Error(
                                    response.code,
                                    response.message,
                                ),
                            )
                        }
                    }
                }
            }
        }

        fun deletePhotos(
            albumId: Long,
            photoIds: List<Long>,
        ) {
            viewModelScope.launch {
                _galleryUiEvent.emit(GalleryUiEvent.Loading)
                galleryRepository.deletePhotos(albumId, photoIds).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _galleryUiEvent.emit(GalleryUiEvent.Success)
                            getOneAlbum(albumId)
                        }

                        is ApiResponse.Error -> {
                            _galleryUiEvent.emit(
                                GalleryUiEvent.Error(
                                    response.code,
                                    response.message,
                                ),
                            )
                        }
                    }
                }
            }
        }

        fun downloadImage(urls: List<String>) {
            viewModelScope.launch {
                try {
                    if (checkPermissions()) {
                        withContext(Dispatchers.IO) {
                            val downloadManager =
                                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                            urls.forEach { url ->
                                val request =
                                    DownloadManager
                                        .Request(Uri.parse(url))
                                        .setTitle("패밀링 사진 \uD83D\uDCF8")
                                        .setDescription("이미지 다운로드 중...")
                                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                        .setDestinationInExternalPublicDir(
                                            Environment.DIRECTORY_PICTURES,
                                            "Familring/${System.currentTimeMillis()}.jpg",
                                        ).setAllowedOverMetered(true)
                                        .setAllowedOverRoaming(true)
                                downloadManager.enqueue(request)
                            }
                        }
                    }
                } catch (e: Exception) {
                    Timber.e(e, "이미지 다운로드 실패")
                }
            }
        }

        private fun checkPermissions(): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                return true
            }

            return ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            ) == PackageManager.PERMISSION_GRANTED
        }
    }
