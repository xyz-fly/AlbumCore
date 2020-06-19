[![Build Status](https://travis-ci.com/xyz-fly/AlbumCore.svg?branch=master)](https://travis-ci.com/xyz-fly/AlbumCore)
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
    implementation 'com.github.xyz-fly:albumcore:0.9.2'
    // it need depend on androidx-paging for new features
    implementation 'androidx.paging:paging-runtime-ktx:2.1.2'
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
- 1: Create a RecyclerView and PagedListAdapter.  
- 2: Create a Factory of Paging-DataSource by AlbumFactory.getAlbumDataSource.  
- 3: Get media data by directory-id (get all of media when directory-id is 0).
Simple use cases will look something like this:   
```kotlin
// For a simple image list
AlbumFactory.getAlbumDataSource(
    this,
    MediaTypeSelection.Builder().setBucketId(id).image().video().create()
).toLiveData(
    config = config, // this is PagedList.Config
    boundaryCallback = object : PagedList.BoundaryCallback<AlbumMedia>() {
    }).observe(this, Observer {
    // submit list by PagedListAdapter
})
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
