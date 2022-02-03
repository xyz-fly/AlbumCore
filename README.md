[![Build Status](https://app.travis-ci.com/xyz-fly/AlbumCore.svg?branch=master)](https://app.travis-ci.com/xyz-fly/AlbumCore)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.xyz-fly/albumcore/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.xyz-fly/albumcore)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)
[![MinSdk](https://img.shields.io/badge/%20MinSdk%20-%2016%2B%20-f0ad4e.svg)](https://android-arsenal.com/api?level=16)

# AlbumCore
A fast media loader library depend on androidx-paging without UI widget for Android. 

![AlbumCore](https://raw.github.com/xyz-fly/AlbumCore/master/static/album_1.gif) ![AlbumCore](https://raw.github.com/xyz-fly/AlbumCore/master/static/album_2.png)

# Download
So just add the dependency to your project build.gradle file:
```groovy
dependencies {
    implementation 'com.github.xyz-fly:albumcore:0.9.9'
    // it need depend on androidx-paging for new features
    implementation 'androidx.paging:paging-runtime-ktx:3.1.0'
}
```
# Sample usage
A sample project which provide runnable code examples

## Permission
Add the permission to your project manifests, and request permissions in your activity:
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
```

## Get Media of Directory
Create a loader by loadmanager and set DirectoryCallback as callback like this:  
```kotlin
// For a simple directory list
LoaderManager.getInstance(this).initLoader(0, null, object : DirectoryCallback(
    this,
    MediaTypeSelection.Builder().image().video().create()
) {
    override fun onLoadComplete(
        loader: Loader<Cursor>,
        list: List<AlbumDirectory>
    ) {
        // show list to view
    }
})
```

## Get media
- 1: Create RecyclerView and PagingDataAdapter.
- 2: Create a Pager by AlbumFactory, then create a flow to submit list data
- 3: Get media data by directory-id (get all of media when directory-id is 0).
Simple use cases will look something like this:   
```kotlin
// For a simple image list

private val clearListCh = Channel<Unit>(Channel.CONFLATED)
private val albumMediaId = MutableLiveData<Long>()

private val posts = flowOf(
    clearListCh.receiveAsFlow().map { PagingData.empty<AlbumMedia>() },
    albumMediaId.asFlow().flatMapLatest {
        Pager(config) {
            AlbumFactory.getAlbumPagingSource(
                this,
                MediaTypeSelection.Builder().setBucketId(it).image().video().create()
            )
        }.flow.onStart {
            // let clearListCh emit empty-list first
            delay(1L)
        }
    }
).flattenMerge(2)

//
lifecycleScope.launchWhenCreated {
    posts.collectLatest {
        adapter.submitData(it)
    }
}
```

## Filter media format
Filters items by an Array, or Ignore items by an Array
```kotlin
MediaTypeSelection.Builder()
    .setBucketId(id)
    .image(filter = arrayOf(MimeType.IMAGE_JPG, MimeType.IMAGE_PNG, MimeType.IMAGE_WEBP))
    .video(ignore = arrayOf(MimeType.VIDEO_AVI))
    .create()
```

## Use selection and args
Use selection as query media parameter
```kotlin
val selection = "${MediaStore.Video.Media.DURATION} >= ?"
val selectionArgs = arrayOf(
    TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString()
)

MediaTypeSelection.Builder()
    .selection(selection, selectionArgs)
    .create()
```

# License

    Copyright 2020 xyz-fly

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
