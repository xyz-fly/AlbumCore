package com.leon.album.core

/**
 * See this http://www.iana.org/assignments/media-types/media-types.xhtml
 */
object MimeType {

    // normal image
    const val IMAGE_JPG = "image/jpeg"
    const val IMAGE_PNG = "image/png"
    const val IMAGE_BMP = "image/x-ms-bmp"
    const val IMAGE_WEBP = "image/webp"
    const val IMAGE_HEIF = "image/heif"
    const val IMAGE_GIF = "image/gif"

    // raw image
    const val IMAGE_DNG = "image/x-adobe-dng"
    const val IMAGE_CR2 = "image/x-canon-cr2"
    const val IMAGE_NEF = "image/x-nikon-nef"
    const val IMAGE_NRW = "image/x-nikon-nrw"
    const val IMAGE_ARW = "image/x-sony-arw"
    const val IMAGE_RW2 = "image/x-panasonic-rw2"
    const val IMAGE_ORF = "image/x-olympus-orf"
    const val IMAGE_RAF = "image/x-fuji-raf"
    const val IMAGE_PEF = "image/x-pentax-pef"
    const val IMAGE_SRW = "image/x-samsung-srw"

    // video
    const val VIDEO_MSVIDEO = "video/msvideo" // avi
    const val VIDEO_AVI = "video/avi"
    const val VIDEO_MPEG = "video/mpeg"
    const val VIDEO_X_MPEG = "video/x-mpeg"
    const val VIDEO_QUICK_TIME = "video/quicktime" // .mov
    const val VIDEO_MP4 = "video/mp4"
    const val VIDEO_3GP = "video/3gpp"
    const val VIDEO_MOV = "video/mov"
    const val VIDEO_H263 = "video/H263"
    const val VIDEO_H264 = "video/H264"
    const val VIDEO_VIVO = "video/vivo"
    const val VIDEO_VND_VIVO = "video/vnd.vivo"
    const val VIDEO_X_MS_ASF = "video/x-ms-asf"
    const val VIDEO_X_SGI_MOVIE = "video/x-sgi-movie" // movie
    const val VIDEO_AVS_VIDEO = "video/avs-video"
    const val VIDEO_X_MS_WMV = "video/x-ms-wmv" // wmv
}
