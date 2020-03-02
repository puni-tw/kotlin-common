## USAGE

* add dependency to your gradle project

``` 
## build.gradle.kts

repositories {
    maven("https://dl.bintray.com/puni-tw/maven")
}

dependencies {
    val kotlinCommonVersion = "x.x.xx"
    implementation("puni:kotlin-common-spring-boot-data-jpa:$kotlinCommonVersion")
    kapt("puni:kotlin-common-spring-boot-data-jpa-annotation-processor:$kotlinCommonVersion")
}

```

* define entity
```

## Book.kt

@Entity
class Book(
  var name: String = "",
  var price: Int = 0,
  @ManyToOne(targetEntity = Author::class)
  var author: Author = Author(),
  var releaseAt: LocalDateTime = LocalDateTime.now()
) : AutoIdEntity()

```

```
## Author.kt

@Entity
class Author(
  var name: String = "",
  var country: String = ""
) : AutoIdEntity()

```

* define DAO

```
## BookDao.kt

interface BookDao : AutoIdDao<Book>
```


* Run kapt

```
./gradlew kaptKotlin
```

* Now you will be able to query easily by using `Dao.search` method with auto generated field properties

```
fun countByNameAndAuthorName(name: String, authorName: String): Int {
  return bookDao
    .search { book ->
      book.name().eq(name)
        .author().name().eq(authorName)
        .price().gt(1000)
        .releaseAt().lt(LocalDateTime.now())
    }
    .size
}

```
