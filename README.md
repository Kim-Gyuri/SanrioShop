# 프로젝트 소개 :산리오 굿즈거래 플랫폼
### 프로젝트 목표: 취향과 목적을 연결하는 검색
기존의 상품명이나 가격순 검색 방식만으로는 이용자의 다양한 요구를 충분히 충족하기 어렵다고 생각했습니다. <br>
그래서 취향과 목적에 맞는 상품을 찾아갈 수 있도록 검색 기능을 구현하는 것을 목표로 했습니다. 

+  태그 기능 도입
    + 이용자가 원하는 상품을 더 쉽게 찾을 수 있도록 취향 태그 기능을 도입했습니다. <br> 스티커 상품을 예로 들면, '클리어 스티커', '컷팅 스티커', '네임 스티커', '홀로그램 스티커', '종이 스티커', '무광/유광 스티커' 등 다양한 추천 태그를 활용할 수 있도록 설계했습니다. <br><br>
    + 서버에서 상품에 적합한 추천 태그를 제시함으로써, 이용자가 상품 등록 시 상세한 정보를 입력하도록 유도했습니다. <br>
    + 또한, 이용자가 직접 태그를 작성할 수 있는 기능도 제공하여, 상품을 더욱 효과적으로 검색할 수 있도록 구현했습니다. 

+  테마 검색
   + 이용자의 목적과 감성에 부합하는 콘텐츠를 제공하기 위해 카테고리 페이지를 추가했습니다. <br>
   + 이 페이지에서는 '인기 검색어', '포카 꾸미기', '책상 꾸미기', '다꾸/문구', '필기류', '소품' 등의 테마를 통해 <br> 이용자가 원하는 상품을 쉽게 찾을 수 있도록 구성했습니다.

### 차별성
태그 기반 검색을 통해 원하는 상품을 쉽게 찾을 수 있도록 했으며, <br>
'나만의 데스크 꾸미기'와 같은 주제를 원할 때, '책상 꾸미기' 테마 검색을 통해 손쉽게 관련 상품을 찾을 수 있도록 구현했습니다. <br><br>

특히, 가격보다는 제품명과 태그를 강조하여 구매에 대한 강요받는 느낌 없이 자연스럽게 상품을 탐색할 수 있도록 UI 디자인을 설계했습니다. 
이를 통해 상품에 대한 흥미를 높이고, 편안한 탐색 경험을 제공하고자 했습니다.

# ERD
![산리오](https://github.com/user-attachments/assets/6d7ae7b4-c051-452a-9945-2c7dc16b7a2e)
# Technology Stacks
` Backend`
+ Java 21 (OpenJDK)
+ Spring Boot 3.3.1
+ Spring Data JPA
+ Hibernate
+ Spring Web
+ Spring Security
+ Validation
+ Lombok
+ Querydsl
+ JWT
+ Swagger
+ Github
+ AWS 
> EC2: Ubuntu Server t2.micro 프리티어 선택 <br>
> RDS: MySQL Community(엔진), db.t4g.micro(클래스)

> IntelliJ IDEA 24.2 버전, Java 21 버전을 사용하였습니다.

> JWT 구현에 필요한 io.jsonwebtoken:jjwt 라이브러리는 0.12.3 버전을 사용했습니다.

> Spring Boot 3.3.1 버전을 사용하기 때문에, 호환성을 고려하여 Swagger UI 라이브러리로 org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2 버전을 선택하였습니다.

> 데이터베이스로 MySQL을 사용하였으며, 데이터 관리를 위해 주로 MySQL Workbench를 활용하였습니다. 

<br>

`Frontend`
+ Bootstrap template
+ Javascript, Html, Css, Bootstrap 4.5.2, Jquery-3.6.0

> Bootstrap 템플릿에서 사용된 CSS가 4.5.2 버전이어서 동일한 버전을 그대로 적용하였으며, AJAX 요청 처리를 위해 jQuery 3.6.0을 활용하였다.


# Architecture
![아키텍처 drawio](https://github.com/user-attachments/assets/0db90f68-302c-4d27-b736-adff36078d43) <br><br><br>
GitHub Actions를 사용해 CI/CD 파이프라인을 구성했으며, Docker를 활용해 프로젝트 이미지를 빌드하고 컨테이너를 실행하는 방식으로 배포를 진행했습니다.

+ JAVA 21
+ Gradle
+ SpringBoot 3.3.1
+ AWS
  + EC2: Ubuntu Server t2.micro 프리티어 선택
  + RDS: MySQL Community(엔진), db.t4g.micro(클래스)
+ Docker Hub
+ Google Cloud Platform
  + 웹 서비스에서 회원 프로필과 상품 이미지를 등록 및 관리하고 있습니다.
 
# Branches
+ develop : 개발 및 배포를 위한 브랜치로 사용 중
+ master : .

원래는 develop 브랜치를 개발과 테스트를 위한 용도로, master 브랜치를 안정적인 배포 용도로 구분하려 했지만, <br>
현재는 develop 브랜치만 사용 중입니다.

# 트러블 슈팅 & 리팩토링
##  📌 CascadeType.ALL과 orphanRemoval로 인한 외래 키 제약 위반 해결
찜 상품을 찜목록에서 삭제할 때 다음과 같은 오류가 발생했다. <br>
<img src="https://github.com/user-attachments/assets/e5d123ac-d32e-4220-a90e-31c99add351c" width="90%" />
<img src="https://github.com/user-attachments/assets/efcd54d2-c333-42a1-b3cc-8439d71fc146" width="90%" />

SQLIntegrityConstraintViolationException과 DataIntegrityViolationException 두 가지 예외가 발생했다. <br>
메시지를 확인해본 결과, item 엔티티가 삭제될 때 연관된 user_defined_tag 엔티티도 삭제되지만, 외래 키 제약 조건을 위반하는 방식으로 삭제가 시도되어 발생한 오류임을 알 수 있었다.  <br><br>

오류를 찾기 위해서 일단 item 클래스 코드를 다시 읽어보았다. <br>
<img src="https://github.com/user-attachments/assets/1f0ecdb6-83bc-4cb4-83d1-4241f8ab7b0c" width="90%" /> <br>
item 엔티티의 코드를 다시 확인해본 결과, **CascadeType.ALL**과 **orphanRemoval = true**가 함께 설정되어 있었다. <br><br>

#### ✔️ CascadeType.ALL과 orphanRemoval = true
CascadeType.ALL과 orphanRemoval = true가 함께 설정된 경우, <br> item 엔티티가 삭제될 때 연관된 user_defined_tag 엔티티도 함께 삭제되지만,
외래 키 제약 조건을 위반하는 방식으로 삭제될 수 있다. <br>
이 문제는 주로 외래 키 무결성 오류에 의해 발생하며, item_id가 여전히 user_defined_tag에 남아있는 상태에서 item 엔티티를 삭제하려 할 때 발생한다.
#### ✔️ 원인 분석
item 엔티티가 삭제될 때, <br>
연관된 자식 엔티티인 user_defined_tag에서 item_id를 참조하는 외래 키 값이 남아 있는 상태로 삭제가 시도되면, 외래 키 제약 조건을 위반하게 된다. <br>
이로 인해 SQLIntegrityConstraintViolationException 및 DataIntegrityViolationException 예외가 발생한 것이다.

#### ✔️ 해결 방법
<img src="https://github.com/user-attachments/assets/283b1b48-5335-4421-970b-285ff8ca3bf9" width="70%" /> 

CascadeType.ALL은 모든 작업(persist, merge, remove 등)을 자식 엔티티에 전파하지만, 이로 인해 삭제 작업 시 외래 키 제약을 위반하는 경우가 발생할 수 있다. <br>
반면에 CascadeType.REMOVE 으로 변경하면 오직 삭제 작업에만 영향을 미치게 되어, item 삭제 시에만 user_defined_tag가 삭제된다. <br>
또한 다른 작업(persist, merge 등)은 전파되지 않게 된다. 따라서 외래 키 무결성을 위반하지 않게 된다. <br><br>
따라서, CascadeType.REMOVE 으로 설정하면 item을 삭제할 때 연관된 user_defined_tag만 삭제되고, 다른 연관 관계가 있는 엔티티에는 영향을 미치지 않게 된다. <br>이렇게 설정함으로써 외래 키 제약 위반을 방지할 수 있다.


## 📌 검색 기능 구현 중 발생한 데이터 누락 문제와 해결
### ✅ 문제
상품 페이징 조회 기능을 구현하던 중, 예상치 못한 문제가 발생했었다. <br>
API를 통해 데이터를 조회했을 때, DB에서 직접 조회한 결과와 비교해보니 일부 데이터가 누락된 것이다. <br><br>

DB에서 직접 쿼리를 실행했을 때는 12개의 데이터 항목이 모두 정상적으로 조회되었지만, <br>
API를 통해 조회한 결과는 단 6개의 항목만 반환되었다. <br><br><br>

#### POSTMAN 실행
다음과 같이, postman에 페이징 api 요청을 실행했었다.
![image](https://github.com/user-attachments/assets/9914fc2a-5b8e-48ef-86d4-5c85b52b2353)

### ✅ 문제 분석
이 문제를 해결하기 위해, 가장 먼저 살펴본 것은 search_fetch 로직이다.<br>
이 로직은 QueryDSL을 사용하여 페이징 쿼리로 구현했었다.<br>
item, itemImg, userDefinedTag, recommendedTag 네 개의 테이블을 한 번에 조회하는 구조였다.<br>
LEFT JOIN을 사용해 각 테이블을 연결했는데, 이 과정에서 데이터 중복이 발생했을 가능성이 있었던 것 같다.<br><br><br>

#### search_fetch 로직
```
    @Override
    public Page<ItemDtoV3> search_fetch_v5(Pageable pageable, SearchCondition condition) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        QUserDefinedTag userDefinedTag = QUserDefinedTag.userDefinedTag;
        QRecommendedTag recommendedTag = QRecommendedTag.recommendedTag;

        BooleanBuilder whereClause = buildWhereClause(condition, item);

        // Fetch items with pagination and join with tags
        List<Tuple> results = queryFactory
                .select(item.id, item.nameKor, item.price, item.description,
                        item.createAt, item.likeCount, item.sanrioCharacters,
                        item.mainCategory, item.subCategory, itemImg.imgUrl,
                        userDefinedTag.name, recommendedTag.tagOption)
                .from(item)
                .leftJoin(itemImg).on(itemImg.item.eq(item).and(itemImg.isMainImg.eq(IsMainImg.Y)))
                .leftJoin(userDefinedTag).on(userDefinedTag.item.eq(item))
                .leftJoin(recommendedTag).on(recommendedTag.item.eq(item))
                .where(whereClause)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Process the results to group tags by item id
        Map<Long, ItemDtoV3> itemDtoMap = new HashMap<>();

        for (Tuple tuple : results) {
            Long itemId = tuple.get(item.id);
            itemDtoMap.computeIfAbsent(itemId, id -> new ItemDtoV3(
                    id,
                    tuple.get(item.nameKor),
                    tuple.get(item.price),
                    tuple.get(item.description),
                    tuple.get(item.createAt),
                    tuple.get(item.likeCount),
                    tuple.get(item.sanrioCharacters).getNameKor(), // Enum의 nameKor 필드를 사용하여 한글 이름을 가져옵니다.
                    tuple.get(item.mainCategory).getNameKor(),
                    tuple.get(item.subCategory).getNameKor(),
                    tuple.get(itemImg.imgUrl),
                    new ArrayList<>(),
                    new ArrayList<>()
            ));

            ItemDtoV3 itemDto = itemDtoMap.get(itemId);

            String userDefinedTagName = tuple.get(userDefinedTag.name);
            if (userDefinedTagName != null) {
                itemDto.getUserDefinedTags().add(userDefinedTagName);
            }

            String recommendedTagOption = tuple.get(recommendedTag.tagOption) != null ? tuple.get(recommendedTag.tagOption).getNameKor() : null;
            if (recommendedTagOption != null) {
                itemDto.getRecommendedTags().add(recommendedTagOption);
            }
        }

        long total = queryFactory
                .selectFrom(item)
                .leftJoin(itemImg).on(itemImg.item.eq(item).and(itemImg.isMainImg.eq(IsMainImg.Y)))
                .where(whereClause)
                .fetchCount();

        return new PageImpl<>(new ArrayList<>(itemDtoMap.values()), pageable, total);
    }
```

이 로직에서는 여러 테이블을 조인하여 데이터를 조회하기 때문에, 동일한 아이템이 여러 태그와 연관될 경우 데이터 중복이 발생할 수 있다.<br>
"이 중복된 데이터는 원래 반환되어야 할 데이터의 개수를 초과하게 만들 수 있지 않을까?" 라는 생각이 들었다. <br><br><br>

#### DB에 직접 쿼리 실행
문제를 더 명확히 파악하기 위해 <br>
API 요청에서 사용된 쿼리를 로그에서 확인한 뒤, 이를 직접 DB에서 실행해보기로 했다.
```
use sanrio3;

SELECT i1_0.item_id,
       i1_0.create_at,
       i1_0.description,
       iil1_0.item_id AS img_item_id,
       iil1_0.item_img_id,
       iil1_0.img_url,
       iil1_0.is_main_img,
       i1_0.like_count,
       i1_0.main_category,
       i1_0.name_kor,
       i1_0.price,
       rtl1_0.item_id AS rec_tag_item_id,
       rtl1_0.tag_id,
       rtl1_0.tag_frequency,
       rtl1_0.tag_option,
       i1_0.sanrio_characters,
       i1_0.sub_category,
       i1_0.uploader_id,
       udtl1_0.item_id AS user_tag_item_id,
       udtl1_0.item_tag_id,
       udtl1_0.name,
       udtl1_0.tag_frequency
FROM item i1_0
LEFT JOIN item_img iil1_0 ON i1_0.item_id = iil1_0.item_id
LEFT JOIN user_defined_tag udtl1_0 ON i1_0.item_id = udtl1_0.item_id
LEFT JOIN recommended_tag rtl1_0 ON i1_0.item_id = rtl1_0.item_id
WHERE iil1_0.is_main_img = 'Y';
```
![ttttfk](https://github.com/user-attachments/assets/747ab1ac-0eb1-426d-ac36-cf110ab43f83) <br><br>

API 요청 로그를 통해 쿼리를 확인했고, <br>
이를 DB에서 실행해본 결과, 예상대로 중복된 데이터로 인해 일부 데이터가 누락되고 있음을 발견했다. <br><br>

아이템의 ID가 6번까지 조회되었을 때, 이미 페이지 사이즈인 20을 초과했기 때문에, 나머지 데이터는 다음 페이지로 넘어가지 않고 누락되었다. <br><br>

결과적으로, 이 문제는 중복된 데이터가 반환됨으로써 발생한 것이었다. <br>
이 문제를 해결하기 위해서는 데이터 중복을 처리하거나, 쿼리 로직을 개선하여 이러한 상황이 발생하지 않도록 해야 했었다.

### ✅ 해결: 로직 개선
#### ✔️ 기존 로직의 문제점
search_fetch_v5()는 여러 테이블을 조인하여 데이터를 조회하는 방식으로 구현되어 있었다. <br>
item, itemImg, userDefinedTag, recommendedTag 테이블을 LEFT JOIN으로 연결하여 데이터를 조회했는데, 이 과정에서 데이터 중복 문제가 발생했다. <br><br><br>

#### ✔️ 어떻게 개선할까?
데이터 조회와 태그 조회를 분리하고 필요한 데이터를 별도로 그룹핑하여 처리하는 방식으로 해결하고자 했다. <br><br><br>

#### ✔️ 개선된 로직
```java
    @Override
    public Page<ItemDtoV3> search_fetch_v6(Pageable pageable, SearchCondition condition) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        QUserDefinedTag userDefinedTag = QUserDefinedTag.userDefinedTag;
        QRecommendedTag recommendedTag = QRecommendedTag.recommendedTag;

        BooleanBuilder whereClause = new BooleanBuilder();

        if (condition.getMainCategory() != null) {
            whereClause.and(item.mainCategory.eq(condition.getMainCategory()));
        }

        if (condition.getSubCategory() != null) {
            whereClause.and(item.subCategory.eq(condition.getSubCategory()));
        }

        if (condition.getSanrioCharacters() != null) {
            whereClause.and(item.sanrioCharacters.eq(condition.getSanrioCharacters()));
        }

        if (condition.getItemName() != null) {
            String searchTerm = "%" + condition.getItemName().toLowerCase() + "%";
            whereClause.and(item.nameKor.toLowerCase().like(searchTerm));
        }

        // Fetch items with pagination
        List<ItemTemp> items = queryFactory
                .select(new QItemTemp(
                        item.id, item.nameKor, item.price, item.description,
                        item.createAt, item.likeCount, item.sanrioCharacters, item.mainCategory, item.subCategory, itemImg.imgUrl))
                .from(item)
                .leftJoin(itemImg).on(itemImg.item.eq(item).and(itemImg.isMainImg.eq(IsMainImg.Y)))
                .where(whereClause)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Fetch user-defined and recommended tags
        List<Tuple> userDefinedTagTuples = queryFactory
                .select(userDefinedTag.item.id, userDefinedTag.name)
                .from(userDefinedTag)
                .fetch();

        List<Tuple> recommendedTagTuples = queryFactory
                .select(recommendedTag.item.id, recommendedTag.tagOption)
                .from(recommendedTag)
                .fetch();

        // 4. 태그 정보를 Map으로 변환
        Map<Long, List<String>> userDefinedTags = userDefinedTagTuples.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(userDefinedTag.item.id),
                        Collectors.mapping(tuple -> tuple.get(userDefinedTag.name), Collectors.toList())
                ));

        Map<Long, List<String>> recommendedTags = recommendedTagTuples.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(recommendedTag.item.id),
                        Collectors.mapping(
                                tuple -> tuple.get(recommendedTag.tagOption).getNameKor(),
                                Collectors.toList()
                        )
                ));

        // Map ItemTemp to ItemDto
        List<ItemDtoV3> finalItems = items.stream()
                .map(itemTemp -> new ItemDtoV3(
                        itemTemp.getId(),
                        itemTemp.getNameKor(),
                        itemTemp.getPrice(),
                        itemTemp.getDescription(),
                        itemTemp.getCreateAt(),
                        itemTemp.getLikeCount(),
                        itemTemp.getSanrioCharacters().getNameKor(),
                        itemTemp.getMainCategory().getNameKor(),
                        itemTemp.getSubCategory().getNameKor(),
                        itemTemp.getThumbnail(),
                        userDefinedTags.getOrDefault(itemTemp.getId(), List.of()),
                        recommendedTags.getOrDefault(itemTemp.getId(), List.of())
                ))
                .collect(Collectors.toList());

        long total = queryFactory
                .selectFrom(item)
                .leftJoin(itemImg).on(itemImg.item.eq(item).and(itemImg.isMainImg.eq(IsMainImg.Y)))
                .where(whereClause)
                .fetchCount();

        return new PageImpl<>(finalItems, pageable, total);
    }
```

#### 필터링 조건 개선
search_fetch_v6에서는 검색 조건에 따라 동적으로 whereClause를 구성했다. <br>
이를 통해 조회하고자 하는 데이터에 대해 더욱 명확하고 효율적인 필터링을 적용할 수 있었다. 예를 들어, 카테고리, 캐릭터, 이름 등의 조건에 따라 SQL 조건을 유연하게 생성했다. <br><br><br>

#### 데이터 조회 분리
기존 로직에서는 여러 테이블을 한 번에 조인하여 데이터를 가져왔었다. <br>
새로운 로직에서는 아이템 정보와 태그 정보를 분리하여 각각 조회했다. <br>
이를 통해 아이템 리스트를 먼저 조회한 후, 별도로 태그 정보를 가져와 각 아이템에 매핑하는 방식으로 개선했다. <br><br><br>

#### 태그 정보 매핑 및 그룹화
태그 정보는 각각의 아이템 ID를 기준으로 그룹화시켰다. <br>
이 과정에서 userDefinedTag와 recommendedTag의 데이터를 각각 매핑하여, 아이템별로 정확한 태그 정보를 유지할 수 있었다.


### ✔️ DB에 직접 쿼리 실행
API 요청에서 사용된 쿼리를 로그에서 확인한 뒤, 이를 직접 DB에서 실행해보았다.
```
use sanrio3;

SELECT
    i.item_id,
    i.name_kor,
    i.price,
    i.description,
    i.create_at,
    i.like_count,
    i.sanrio_characters,
    i.main_category,
    i.sub_category,
    ii.img_url
FROM
    item i
LEFT JOIN
    item_img ii ON ii.item_id = i.item_id AND ii.is_main_img = 'Y';

SELECT
    udt.item_id,
    udt.name
FROM
    user_defined_tag udt;


SELECT
    rt.item_id,
    rt.tag_option
FROM
    recommended_tag rt;


SELECT
    COUNT(i.item_id)
FROM
    item i
LEFT JOIN
    item_img ii ON ii.item_id = i.item_id AND ii.is_main_img = 'Y';
```
![image](https://github.com/user-attachments/assets/d417c52d-2ab9-45fc-83bd-5bac40b56418)


## 📌 검색 로직 리팩토링
검색 기능을 통해 상품 정보를 조회할 때, 썸네일에 표시되는 정보를 가져오는 조회로직이 필요하다. <br>
썸네일에 포함되어야 하는 정보는 다음과 같다.
+ 상품명
+ 상품 가격
+ 상품 이미지
+ 태그
+ 현재 로그인한 유저가 해당 상품을 찜 등록했을 여부

#### ✔️ 리팩토링 목적
검색 조건은 여러 가지가 있지만, `검색 시 반환되는 데이터 타입이 동일하기 때문에 중복 코드를 제거`하여 리팩토링할 필요가 있었다. <br>
검색 조건은 다음과 같다.
+ 태그
+ 상품명
+ 산리오 캐릭터 구분
+ 테마 검색


###  ✅ 리팩토링 전 코드
#### Item 엔티티의 연관관계
<img src="https://github.com/user-attachments/assets/9e81ab8b-b30b-4872-bf75-3c9e572556f8" width="90%" />  <br>

#### 조회로직
```java

    @Override
    public Page<ItemDtoV5> search_fetch_v8(Pageable pageable, SearchCondition condition, String userEmail) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;
        QUserDefinedTag userDefinedTag = QUserDefinedTag.userDefinedTag;
        QRecommendedTag recommendedTag = QRecommendedTag.recommendedTag;
        QUser user = QUser.user;

        BooleanBuilder whereClause = new BooleanBuilder();

        if (condition.getMainCategory() != null) {
            whereClause.and(item.mainCategory.eq(condition.getMainCategory()));
        }

        if (condition.getSubCategory() != null) {
            whereClause.and(item.subCategory.eq(condition.getSubCategory()));
        }

        if (condition.getSanrioCharacters() != null) {
            whereClause.and(item.sanrioCharacters.eq(condition.getSanrioCharacters()));
        }

        if (condition.getItemName() != null) {
            String searchTerm = "%" + condition.getItemName().toLowerCase() + "%";
            whereClause.and(item.nameKor.toLowerCase().like(searchTerm));
        }

        // Fetch items with pagination
        List<ItemTemp> items = queryFactory
                .select(new QItemTemp(
                        item.id, item.nameKor, item.price, item.description,
                        item.createAt, item.likeCount, item.sanrioCharacters, item.mainCategory, item.subCategory, itemImg.imgUrl))
                .from(item)
                .leftJoin(itemImg).on(itemImg.item.eq(item).and(itemImg.isMainImg.eq(IsMainImg.Y)))
                .where(whereClause)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Fetch user-defined and recommended tags
        List<Tuple> userDefinedTagTuples = queryFactory
                .select(userDefinedTag.item.id, userDefinedTag.name)
                .from(userDefinedTag)
                .fetch();

        List<Tuple> recommendedTagTuples = queryFactory
                .select(recommendedTag.item.id, recommendedTag.tagOption)
                .from(recommendedTag)
                .fetch();

        List<Tuple> likerTuples = queryFactory
                .select(user.item.id, user.email)
                .from(user)
                .fetch();

        // 태그 정보를 Map으로 변환
        Map<Long, List<String>> userDefinedTags = userDefinedTagTuples.stream()
                .collect(Collectors.groupingBy(
                        tuple -> Optional.ofNullable(tuple.get(userDefinedTag.item.id)).orElse(-1L),
                        Collectors.mapping(tuple -> Optional.ofNullable(tuple.get(userDefinedTag.name)).orElse(""), Collectors.toList())
                ));
        userDefinedTags.remove(-1L); // -1로 대체된 키를 제거

        Map<Long, List<String>> recommendedTags = recommendedTagTuples.stream()
                .collect(Collectors.groupingBy(
                        tuple -> Optional.ofNullable(tuple.get(recommendedTag.item.id)).orElse(-1L),
                        Collectors.mapping(
                                tuple -> Optional.ofNullable(tuple.get(recommendedTag.tagOption).getNameKor()).orElse(""),
                                Collectors.toList()
                        )
                ));
        recommendedTags.remove(-1L); // -1로 대체된 키를 제거

        Map<Long, List<String>> likers = likerTuples.stream()
                .collect(Collectors.groupingBy(
                        tuple -> Optional.ofNullable(tuple.get(user.item.id)).orElse(-1L),
                        Collectors.mapping(tuple -> Optional.ofNullable(tuple.get(user.email)).orElse(""), Collectors.toList())
                ));
        likers.remove(-1L); // -1로 대체된 키를 제거

        // Map ItemTemp to ItemDto
        List<ItemDtoV5> finalItems = items.stream()
                .map(itemTemp -> new ItemDtoV5(
                        itemTemp.getId(),
                        itemTemp.getNameKor(),
                        itemTemp.getPrice(),
                        itemTemp.getDescription(),
                        itemTemp.getCreateAt(),
                        itemTemp.getLikeCount(),
                        itemTemp.getSanrioCharacters().getNameKor(),
                        itemTemp.getMainCategory().getNameKor(),
                        itemTemp.getSubCategory().getNameKor(),
                        itemTemp.getThumbnail(),
                        userDefinedTags.getOrDefault(itemTemp.getId(), List.of()),
                        recommendedTags.getOrDefault(itemTemp.getId(), List.of()),
                        likers.getOrDefault(itemTemp.getId(), List.of()),
                        likers.getOrDefault(itemTemp.getId(), List.of()).contains(userEmail)  // isLikedByUser 설정
                ))
                .collect(Collectors.toList());

        long total = queryFactory
                .selectFrom(item)
                .leftJoin(itemImg).on(itemImg.item.eq(item).and(itemImg.isMainImg.eq(IsMainImg.Y)))
                .where(whereClause)
                .fetchCount();

        return new PageImpl<>(finalItems, pageable, total);
    }
```
#### 실행된 쿼리
+ items 리스트를 조회하는 쿼리
+ userDefinedTag, recommendedTag, liker를 각각 별도의 쿼리로 실행

#### ✔️ 문제 분석
+ Item과 User 간의 1:N 관계를 잘 활용하지 못했다.
   + User와 Item 간의 1:N 관계를 잘 활용하지 못하고, 각 테이블에 대한 데이터를 개별적으로 조회했다.
   + User와 Item 간의 관계를 별도의 로직으로 결합하려다 보니 코드가 복잡해졌다.
   + 특히, Item과 User 간의 찜 정보를 Map<Long, List<String>> 형태로 변환하는 과정에서 불필요한 데이터 변환과 중복된 쿼리 실행이 발생했다.
+ 결과적으로 코드가 복잡해지는 문제가 발생했다.
   + Item과 User 간의 관계를 1:N으로 설계했지만, 이 관계를 직접 쿼리로 처리하는 과정에서 오히려 쿼리 성능이 저하되고 코드가 복잡해졌다.
   + 데이터를 Tuple로 조회한 후 이를 Map<Long, List>로 변환하는 과정이 복잡하고 비효율적이다.

 <br><br>

### ✅ 문제1 : Item과 User 간 직접 연관 조회하는 경우, 코드가 복잡해진다.
```java
@OneToMany(mappedBy = "item", orphanRemoval = true)
private List<User> likers = new ArrayList<>(); //찜 등록한 유저들
```
찜 등록한 유저 정보를 가져오기 위해서 Item과 User를 1:N 관계로 설계했으며, <br>
Item에서 찜 등록한 회원 정보를 직접 관리하고 조회하려고 했다.  <br>
Item과 User의 연관 관계에서 직접적인 조회 쿼리를 작성하면 직관적이고 코드가 간결해지지 않을까 생각했다. <br><br><br>

```java
        List<Tuple> likerTuples = queryFactory
                .select(user.item.id, user.email)
                .from(user)
                .fetch();

        ...

        Map<Long, List<String>> likers = likerTuples.stream()
                .collect(Collectors.groupingBy(
                        tuple -> Optional.ofNullable(tuple.get(user.item.id)).orElse(-1L),
                        Collectors.mapping(tuple -> Optional.ofNullable(tuple.get(user.email)).orElse(""), Collectors.toList())
                ));
        likers.remove(-1L); // -1로 대체된 키를 제거
```

Item과 연관된 User 정보를 조회하기 위해 User 엔티티 데이터를 Tuple 형태로 조회하였으며,   <br>
이를 `Map<Long, List<String>>` 형태로 변환하여 반환하도록 구현했다.  <br><br>

이 과정에서 `user.item.id`를 키(pk)로 설정하고, 해당 Item을 찜한 회원들의 이메일 정보를 리스트(List)로 저장했다.   <br>
최종적으로 조회된 데이터를 Map<Long, List<String>> 형태로 변환하여 활용했다.<br><br><br>


#### ✔️ 개선: WishItem을 활용해 관계를 풀기
User와 Item 간의 관계를 통해 찜 등록한 유저와 해당 상품 정보를 조회하는 방식은 사용하지 않기로 했다. <br>
대신 WishItem 엔티티를 활용하여 관계를 풀기로 하였고, 이를 통해 조회 로직을 더 명확하고 직관적으로 만들 수 있었다. <br><br>

참고로 User, Item, WishItem, WishList 간의 연관관계는 다음과 같다.
+ User와 WishList는 1:1 관계
+ WishList와 WishItem은 1:N 관계
+ WishItem은 Item과 N:1 관계
> [Wiki 글 - ERD와 DB 테이블 관계도 정리](https://github.com/Kim-Gyuri/SanrioShop/wiki/%ED%81%B4%EB%9E%98%EC%8A%A4-%EC%84%A4%EA%B3%84%EC%99%80-DB-%ED%85%8C%EC%9D%B4%EB%B8%94-%EC%84%A4%EA%B3%84)에 ERD와 함께 테이블 설계 과정에서 각 테이블 간의 관계와 주요 필드를 어떻게 정의했는지 정리해 두었다. <br>
> 자세한 설명은 해당 페이지 글을 참고.

<br><br>

#### ✔️ 개선된 코드
#### (1) Item, WishItem 엔티티
<img src="https://github.com/user-attachments/assets/d337ceeb-b02b-43f4-83e7-501c4af52f00" width="60%" /> <br> 
<img src="https://github.com/user-attachments/assets/8b9db372-5e3d-4c58-afb5-159632a52a18" width="40%" /><br> 
Item에 있던 User 관계를 지우고, 찜 등록한 회원 정보를 WishItem을 중간 엔티티로 사용하여 조회하기로 했다. <br>
WishItem을 중간 테이블로 활용하여 조인의 범위를 좁히려고 했다. <br><br> <br> 

#### (2) 조회로직
WishItem은 Item과 User의 관계만 다루기 때문에, 쿼리가 더 명확하고 직관적이다. <br>
WishItem을 기준으로 Item과 User 간의 관계를 조인하여 필요한 정보를 한번에 조회하고, Map<Long, List<String>> 형태로 변환할 수 있었다. <br>
<img src="https://github.com/user-attachments/assets/0fa7a7bd-d73a-434f-b745-a8d0536e27b9" width="70%" />  <br> <br>

#### 실행된 쿼리
```
    select
        wi1_0.item_id,
        u1_1.email 
    from
        wish_item wi1_0 
    join
        wish_list wl1_0 
            on wl1_0.wish_list_id=wi1_0.wish_list_id 
    join
        (users u1_0 
    join
        user_base u1_1 
            on u1_0.user_id=u1_1.user_id) 
        on wl1_0.wish_list_id=u1_0.wish_list_id
```

 <br><br>

### ✅ 문제2: 상품 정보 조회 쿼리 코드에서 중복된 부분이 많다.
위의 조회 로직이 실행되는 쿼리는 아래와 같다. <br>
상품과 연관된 데이터를 모두 개별적으로 조회한 후, Stream을 사용하여 조건에 맞는 데이터를 필터링하고 매핑하는 방식을 사용했었다. <br>
하지만 검색 로직에서 해당 부분이 중복되므로 개선이 필요하다고 느꼈다. <br>
```
    select
        i1_0.item_id,
        i1_0.name_kor,
        i1_0.price,
        i1_0.description,
        i1_0.create_at,
        i1_0.like_count,
        i1_0.sanrio_characters,
        i1_0.main_category,
        i1_0.sub_category,
        ii1_0.img_url 
    from
        item i1_0 
    left join
        item_img ii1_0 
            on ii1_0.item_id=i1_0.item_id 
            and ii1_0.is_main_img=? 
    limit
        ?, ?
Hibernate: 
    select
        udt1_0.item_id,
        udt1_0.name 
    from
        user_defined_tag udt1_0
Hibernate: 
    select
        rt1_0.item_id,
        rt1_0.tag_option 
    from
        recommended_tag rt1_0
Hibernate: 
    select
        wi1_0.item_id,
        u1_1.email 
    from
        wish_item wi1_0 
    join
        wish_list wl1_0 
            on wl1_0.wish_list_id=wi1_0.wish_list_id 
    join
        (users u1_0 
    join
        user_base u1_1 
            on u1_0.user_id=u1_1.user_id) 
        on wl1_0.wish_list_id=u1_0.wish_list_id
Hibernate: 
    select
        count(i1_0.item_id) 
    from
        item i1_0 
    left join
        item_img ii1_0 
            on ii1_0.item_id=i1_0.item_id 
            and ii1_0.is_main_img=?
```

#### 실행된 쿼리
여러 번 각각 별도의 쿼리로 조회하고 있다. 
+ 첫 번째 쿼리: item과 item_img 조회
+ 두 번째 쿼리: user_defined_tag 조회
+ 세 번째 쿼리: recommended_tag 조회
+ 네 번째 쿼리: wish_item, wish_list, users, user_base 조회

<br><br><br>

#### ✔️ 쿼리 개선방안
selectFrom(item)을 사용하여 필요한 데이터(상품, 태그 등)를 한 번의 쿼리로 가져오는 방식으로 쿼리의 효율성을 높였다. <br>
또한, 상품 조회 쿼리에서 `distinct`와 `countDistinct`를 사용하여 중복 데이터를 제거하고 성능을 개선했다. <br> <br><br>

#### ✔️ 개선된 코드
코드 가독성을 높이기 위해 DTO 변환 로직을 convertToThumbnailItemDto() 메서드로 분리하여 코드의 간결성과 유지보수성을 향상시켰다.  <br>
![image](https://github.com/user-attachments/assets/3c5de2eb-5c2e-43d9-9256-24b9e0912146) <br> 
![image](https://github.com/user-attachments/assets/c1a8f2d6-5ad1-444b-9ae5-2a3f4ba6039a)  <br><br>

#### 실행된 쿼리
```
Hibernate: 
    select
        distinct i1_0.item_id,
        i1_0.create_at,
        i1_0.description,
        i1_0.like_count,
        i1_0.main_category,
        i1_0.name_kor,
        i1_0.price,
        i1_0.sanrio_characters,
        i1_0.sub_category,
        i1_0.uploader_id 
    from
        item i1_0 
    left join
        recommended_tag rtl1_0 
            on i1_0.item_id=rtl1_0.item_id 
    left join
        user_defined_tag udtl1_0 
            on i1_0.item_id=udtl1_0.item_id 
    where
        i1_0.main_category=? 
        and i1_0.sub_category=? 
        and i1_0.sanrio_characters=? 
        and (
            rtl1_0.tag_option=? 
            or lower(udtl1_0.name) like ? escape '!'
        ) 
    limit
        ?, ?
Hibernate: 
    select
        count(distinct i1_0.item_id) 
    from
        item i1_0 
    left join
        recommended_tag rtl1_0 
            on i1_0.item_id=rtl1_0.item_id 
    left join
        user_defined_tag udtl1_0 
            on i1_0.item_id=udtl1_0.item_id 
    where
        i1_0.main_category=? 
        and i1_0.sub_category=? 
        and i1_0.sanrio_characters=? 
        and (
            rtl1_0.tag_option=? 
            or lower(udtl1_0.name) like ? escape '!'
        )
Hibernate: 
    select
        wi1_0.item_id,
        u1_1.email 
    from
        wish_item wi1_0 
    join
        wish_list wl1_0 
            on wl1_0.wish_list_id=wi1_0.wish_list_id 
    join
        (users u1_0 
    join
        user_base u1_1 
            on u1_0.user_id=u1_1.user_id) 
        on wl1_0.wish_list_id=u1_0.wish_list_id
```


# Wiki Docs
프로젝트 진행 중 작성한 문서 모음.
+ [프로젝트 Overview 바로가기](https://github.com/Kim-Gyuri/SanrioShop/wiki/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-Overview) <br>
+ [레퍼런스앱 분석노트 : 레퍼런스 콜리를 분석한 목적 / 분석을 통한 프로젝트 목적](https://github.com/Kim-Gyuri/SanrioShop/wiki/%EB%A0%88%ED%8D%BC%EB%9F%B0%EC%8A%A4%EC%95%B1-%EB%B6%84%EC%84%9D%EB%85%B8%ED%8A%B8)
+ [API Docs 바로가기](https://github.com/Kim-Gyuri/SanrioShop/wiki/API-Docs)
+ [Features : 최종 구현 기능 바로가기](https://github.com/Kim-Gyuri/SanrioShop/wiki/Features-:-%EC%B5%9C%EC%A2%85-%EA%B5%AC%ED%98%84-%EA%B8%B0%EB%8A%A5)
+ [클래스 설계와 DB 설계 바로가기](https://github.com/Kim-Gyuri/SanrioShop/wiki/%ED%81%B4%EB%9E%98%EC%8A%A4-%EC%84%A4%EA%B3%84%EC%99%80-DB-%ED%85%8C%EC%9D%B4%EB%B8%94-%EC%84%A4%EA%B3%84)
+ [서비스별 시퀀스 다이어그램 바로가기](https://github.com/Kim-Gyuri/SanrioShop/wiki/%EC%84%9C%EB%B9%84%EC%8A%A4%EB%B3%84-%EC%8B%9C%ED%80%80%EC%8A%A4-%EB%8B%A4%EC%9D%B4%EC%96%B4%EA%B7%B8%EB%9E%A8)
+ [테스트 코드정리 바로가기](https://github.com/Kim-Gyuri/SanrioShop/wiki/%ED%85%8C%EC%8A%A4%ED%8A%B8-%EC%BD%94%EB%93%9C)
+ [프로젝트 진행하면서 학습한 내용 바로가기](https://github.com/Kim-Gyuri/SanrioShop/wiki/%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%A7%84%ED%96%89%ED%95%98%EB%A9%B4%EC%84%9C-%ED%95%99%EC%8A%B5%ED%95%9C-%EB%82%B4%EC%9A%A9)
+ [Docker와 GitHub Actions로 서버를 자동화하며 배운 것들 바로가기](https://github.com/Kim-Gyuri/SanrioShop/wiki/Docker%EC%99%80-GitHub-Actions%EB%A1%9C-%EC%84%9C%EB%B2%84%EB%A5%BC-%EC%9E%90%EB%8F%99%ED%99%94%ED%95%98%EB%A9%B0-%EB%B0%B0%EC%9A%B4-%EA%B2%83%EB%93%A4)
  
### 블로그에 업로드한 학습 포스팅
+ [거래요청에 대한 동시성 테스트](https://thumper.tistory.com/72)
+ [다중 토큰: Refresh 토큰과 생명 주기](https://thumper.tistory.com/71)
+ [검색 기능 구현 중 발생한 데이터 누락 문제와 해결](https://thumper.tistory.com/70)
+ [검색 로직 리팩토링](https://thumper.tistory.com/73)

    
