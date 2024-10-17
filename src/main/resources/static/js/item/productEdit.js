const subCategories = {
    POPULAR_SEARCH: [
        { name: "인형", value: "DOLL" },
        { name: "키링/참", value: "KEYRING_CHARM" },
        { name: "브로치/뱃지", value: "BROOCH_BADGE" },
        { name: "아크릴 스탠드", value: "ACRYLIC_STAND" },
        { name: "캡슐 토이", value: "CAPSULE_TOY" },
        { name: "피규어", value: "FIGURE" },
        { name: "카드형", value: "CARD_TYPE" }
    ],

    PHOTOCARD_DECOR: [
        { name: "포토카드 홀더", value: "PHOTOCARD_HOLDER" },
        { name: "탑로더 슬리브", value: "TOPLOADER_SLEEVE" }
    ],

    DESK_DECOR: [
        { name: "책상 피규어", value: "DESK_FIGURE" },
        { name: "가습기", value: "HUMIDIFIER" },
        { name: "키보드", value: "KEYBOARD" },
        { name: "키캡", value: "KEYCAP" },
        { name: "마우스", value: "MOUSE" },
        { name: "책상 시계", value: "DESK_CLOCK" },
        { name: "핸디 팬", value: "HANDY_FAN" },
        { name: "블루투스", value: "BLUETOOTH" },
        { name: "마우스 패드", value: "MOUSE_PAD" },
        { name: "에어 프레셔너", value: "AIR_FRESHENER" },
        { name: "USB 멀티 허브", value: "USB_MULTI_HUB" }
    ],

    JOURNAL_STATIONERY: [
        { name: "스티커", value: "STICKER" },
        { name: "마스킹 테이프", value: "MASKING_TAPE" },
        { name: "메모 패드", value: "MEMO_PAD" },
        { name: "편지지", value: "LETTER_PAPER" },
        { name: "엽서", value: "POSTCARD" },
        { name: "스탬프", value: "STAMP" },
        { name: "다이어리", value: "DIARY" },
        { name: "캘린더", value: "CALENDAR" },
        { name: "노트", value: "NOTE" },
        { name: "파일 바인더", value: "FILE_BINDER" },
        { name: "펜 홀더", value: "PEN_HOLDER" },
        { name: "사무용품", value: "OFFICE_SUPPLIES" },
        { name: "폰 스트랩", value: "PHONE_STRAP" }
    ],

    WRITING_INSTRUMENTS: [
        { name: "볼펜", value: "BALLPOINT_PEN" },
        { name: "샤프펜슬", value: "MECHANICAL_PENCIL" }
    ],

    ACCESSORIES: [
        { name: "헤어 아이템", value: "HAIR_ITEM" },
        { name: "거울", value: "MIRROR" },
        { name: "지갑", value: "WALLET" },
        { name: "가방", value: "BAG" },
        { name: "수납/정리", value: "STORAGE_ORGANIZATION" },
        { name: "쿠션/담요", value: "CUSHION_BLANKET" },
        { name: "자석", value: "MAGNET" },
        { name: "파우치/케이스", value: "POUCH_CASE" }
    ]
};

const tagGroups = {
    STICKER: [
        { name: "클리어 스티커", value: "CLEAR_STICKER" },
        { name: "컷팅 데코 스티커", value: "CUTTING_DECO_STICKER" },
        { name: "페이퍼 스티커", value: "PAPER_STICKER" },
        { name: "금박 스티커", value: "GOLD_FOIL_STICKER" },
        { name: "입체 스티커", value: "THREE_D_STICKER" },
        { name: "네임 스티커", value: "NAME_STICKER" },
        { name: "홀로그램 스티커", value: "HOLOGRAM_STICKER" },
        { name: "무광 스티커", value: "MATTE_STICKER" },
        { name: "유광 스티커", value: "GLOSSY_STICKER" }
    ],

    MEMO_PAD: [
        { name: "떡메모지", value: "PAD_MEMO" },
        { name: "점착 메모지", value: "STICKY_MEMO" },
        { name: "링메모지", value: "RING_MEMO" }
    ],

    STAMP: [
        { name: "단일 스탬프", value: "SINGLE_STAMP" },
        { name: "회전 스탬프", value: "ROTATING_STAMP" },
        { name: "우드 스탬프", value: "WOOD_STAMP" },
        { name: "클리어 스탬프", value: "CLEAR_STAMP" },
        { name: "스탬프 세트", value: "STAMP_SET" },
        { name: "스탬프 케이스", value: "STAMP_CASE" }
    ],

    DIARY: [
        { name: "패브릭 북 커버", value: "FABRIC_BOOK_COVER" },
        { name: "양장 다이어리", value: "HARDCOVER_DIARY" },
        { name: "A4", value: "A4_SIZE" },
        { name: "A5", value: "A5_SIZE" },
        { name: "A6", value: "A6_SIZE" },
        { name: "A7", value: "A7_SIZE" },
        { name: "B5", value: "B5_SIZE" },
        { name: "B6", value: "B6_SIZE" },
        { name: "B7", value: "B7_SIZE" },
        { name: "링 타입", value: "RING_TYPE_DIARY" },
        { name: "노트 타입", value: "NOTE_TYPE_DIARY" }
    ],


    NOTE: [
        { name: "무지", value: "PLAIN_NOTE" },
        { name: "라인", value: "LINED_NOTE" },
        { name: "그리드", value: "GRID_NOTE" },
        { name: "도트", value: "DOT_NOTE" },
        { name: "A4", value: "A4_SIZE" },
        { name: "A5", value: "A5_SIZE" },
        { name: "B5", value: "B5_SIZE" },
        { name: "B6", value: "B6_SIZE" },
        { name: "B7", value: "B7_SIZE" }
    ],

    FILE_BINDER: [
        { name: "A5 파일", value: "A5_FILE" },
        { name: "A4 파일/L홀더", value: "A4_FILE_L_HOLDER" },
        { name: "인덱스형 파일", value: "INDEX_FILE" },
        { name: "지퍼형 파일", value: "ZIPPER_FILE" }
    ],

    PEN_HOLDER: [
        { name: "아크릴 펜꽂이", value: "ACRYLIC_PEN_HOLDER" },
        { name: "플라스틱 펜꽂이", value: "PLASTIC_PEN_HOLDER" }
    ],

    OFFICE_SUPPLIES: [
        { name: "칼/가위", value: "KNIFE_SCISSORS" },
        { name: "마스킹 테이프", value: "CORRECTION_TAPE" },
        { name: "자", value: "RULER" },
        { name: "클립/집게", value: "CLIP_PIN" },
        { name: "스테이플러", value: "STAPLER" }
    ],

    PHONE: [
        { name: "케이블 정리", value: "CABLE_ORGANIZER" },
        { name: "폰케이스", value: "PHONE_CASE" },
        { name: "스마트톡", value: "PHONE_GRIP" }
    ],

    BALLPOINT_PEN: [
        { name: "0.7mm", value: "PEN_0_7MM" },
        { name: "0.6mm", value: "PEN_0_6MM" },
        { name: "0.5mm", value: "PEN_0_5MM" },
        { name: "0.4mm", value: "PEN_0_4MM" },
        { name: "0.3mm", value: "PEN_0_3MM" },
        { name: "2색", value: "PEN_2_COLOR" },
        { name: "3색", value: "PEN_3_COLOR" },
        { name: "4색", value: "PEN_4_COLOR" },
        { name: "5색", value: "PEN_5_COLOR" },
        { name: "챰 볼펜", value: "CHARM_BALLPOINT_PEN" },
        { name: "마스코트 볼펜", value: "MASCOT_BALLPOINT_PEN" }
    ],

    MECHANICAL_PENCIL: [
        { name: "노크 연필", value: "KNOCK_PENCIL" },
        { name: "챰 샤프", value: "CHARM_SHARP" },
        { name: "마스코트 샤프", value: "MASCOT_SHARP" },
        { name: "0.3mm", value: "PEN_0_3MM" },
        { name: "0.5mm", value: "PEN_0_5MM" }
    ],

    COLOR_DECO_PEN: [
        { name: "글리터펜", value: "GLITTER_PEN" },
        { name: "사인펜", value: "SIGN_PEN" }
    ],

    HAIR_ITEM: [
        { name: "헤어 악세서리", value: "HAIR_ACCESSORY" },
        { name: "브러쉬", value: "BRUSH" },
        { name: "세안밴드", value: "FACIAL_BAND" },
        { name: "머리띠", value: "HEADBAND" }
    ],

    MIRROR: [
        { name: "미니 스탠드", value: "MINI_STAND_MIRROR" },
        { name: "거울 키링", value: "MIRROR_KEYRING" },
        { name: "빗&거울", value: "COMB_MIRROR" },
        { name: "휴대용", value: "PORTABLE_MIRROR" },
        { name: "스탠드 거울", value: "STAND_MIRROR" }
    ],

    WALLET: [
        { name: "동전지갑", value: "COIN_WALLET" },
        { name: "반지갑", value: "FOLD_WALLET" },
        { name: "클리어 타입", value: "CLEAR_TYPE_WALLET" },
        { name: "장지갑", value: "LONG_WALLET" }
    ],

    BAG: [
        { name: "숄더백", value: "SHOULDER_BAG" },
        { name: "토트백", value: "TOTE_BAG" },
        { name: "클리어 타입", value: "CLEAR_TYPE_BAG" },
        { name: "인형가방", value: "DOLL_BAG" },
        { name: "얼굴 모양", value: "FACE_SHAPE_BAG" },
        { name: "에코백", value: "ECO_BAG" },
        { name: "미니 숄더백", value: "MINI_SHOULDER_BAG" },
        { name: "매쉬 타입", value: "MESH_TYPE_BAG" },
        { name: "파우치 타입", value: "POUCH_TYPE_BAG" },
        { name: "미니", value: "MINI_BAG" },
        { name: "체크무늬", value: "CHECK_PATTERN_BAG" },
        { name: "장바구니", value: "BASKET_BAG" }
    ],

    STORAGE_ORGANIZATION: [
        { name: "화장품 보관", value: "COSMETIC_STORAGE" },
        { name: "지퍼백", value: "ZIPPER_BAG" },
        { name: "트레이", value: "TRAY" },
        { name: "접이식", value: "FOLDABLE" },
        { name: "플라스틱", value: "PLASTIC_STORAGE" },
        { name: "스테인", value: "STAIN_STORAGE" },
        { name: "바구니", value: "BASKET" },
        { name: "컬렉션 수납함", value: "COLLECTION_STORAGE_BOX" },
        { name: "아크릴 타입", value: "ACRYLIC_TYPE" }
    ],

    CUSHION_BLANKET: [
        { name: "타올", value: "TOWEL" },
        { name: "여름 담요", value: "SUMMER_BLANKET" },
        { name: "겨울 담요", value: "WINTER_BLANKET" }
    ],

    POUCH_CASE: [
        { name: "펜", value: "PEN_CASE" },
        { name: "얼굴 모양", value: "FACE_SHAPE_CASE" },
        { name: "복조리", value: "SEWING_CASE" },
        { name: "클리어 타입", value: "CLEAR_TYPE_CASE" },
        { name: "미니", value: "MINI_CASE" },
        { name: "봉제", value: "SEWN_CASE" },
        { name: "납작", value: "FLAT_CASE" },
        { name: "입체", value: "THREE_D_CASE" },
        { name: "안경", value: "GLASSES_CASE" },
        { name: "지퍼백", value: "ZIPPER_BAG_CASE" }
    ],
};

$(document).ready(function () {
    // 초기 설정
    const itemId = localStorage.getItem('selectedItemId');
    let accessToken = localStorage.getItem('accessToken');
    accessToken = accessToken ? accessToken.trim() : '';

    var hashtag = {};
    var counter = 0;
    var existingFiles = []; // 기존 파일 목록을 저장할 배열
    var selectedTags = new Set(); // 선택된 태그를 저장할 Set

    // 상품 정보를 가져오기
    $.ajax({
        url: `/api/items/product/${itemId}/edit`,
        method: 'GET',
        headers: { 'Authorization': accessToken },
        contentType: 'application/json',
        success: function (data) {
            populateForm(data); // 폼 데이터 채우기
            populateImages(data.imgList); // 이미지 미리보기 표시
        },
        error: function (xhr) {
            console.error('상품 정보를 불러오는 과정에서 오류가 발생했습니다.:', xhr.status);
        }
    });

    // 상품 정보 폼에 채우기
    function populateForm(item) {
        $('#productName').val(item.nameKor); // 상품명
        $('#productDescription').val(item.description); // 상세정보
        $('#productPrice').val(item.price); // 가격
        $('#main-category').val(item.mainCategory); // 상위 카테고리
        $('#productCategory').val(item.sanrioCharacters); // 산리오 캐릭터 정보

        populateSubCategories(item.mainCategory, item.subCategory); // 하위 카테고리 표기
        populateTags(item.userDefinedTags, item.recommendedTags); // 해시태그 정보

        // 태그 옵션 업데이트
        updateTagOptions(item.subCategory);
    }

    // 메인 카테고리 변경 시 서브 카테고리와 태그 가져오기
    $('#main-category').change(function () {
        const selectedMainCategory = $(this).val();
        populateSubCategories(selectedMainCategory);
        updateTagOptions(); // 메인 카테고리가 변경될 때 태그 옵션도 업데이트
    });

    // 서브 카테고리 변경 시 태그 옵션 업데이트
    $('#sub-category').change(function () {
        updateTagOptions(); // 서브 카테고리가 변경될 때 태그 옵션도 업데이트
    });

    // 태그 옵션 업데이트
    function updateTagOptions() {
        const subCategory = document.getElementById('sub-category').value;
        const tagContainer = document.getElementById('tag-container');

        tagContainer.innerHTML = ''; // 기존 내용 비우기

        if (subCategory && tagGroups[subCategory]) {
            tagGroups[subCategory].forEach(tagOption => {
                // 자바스크립트로 버튼 생성
                const button = document.createElement('button');
                button.className = 'tag-button'; // CSS 클래스 적용
                button.textContent = tagOption.name; // 버튼에 표시될 텍스트 설정
                button.dataset.value = tagOption.value; // 버튼의 고유 값을 data-value 속성에 저장

                // 버튼에 클릭 이벤트 리스너를 추가하여 선택 토글 기능 구현
                button.onclick = function(event) {
                    event.preventDefault();
                    toggleTagSelection(button);
                };

                // 버튼을 태그 컨테이너에 추가
                tagContainer.appendChild(button);
            });
        } else {
            // 태그 옵션이 없을 경우 메시지 표시
            tagContainer.innerHTML = '<p>추천 태그가 없어요!</p>';
        }
    }

    // 태그 선택 토글 함수 (자바스크립트로 수정)
    function toggleTagSelection(button) {
        button.classList.toggle('selected');
        const tagValue = button.dataset.value; // 자바스크립트로 data 속성 값 가져오기
        if (button.classList.contains('selected')) {
            selectedTags.add(tagValue);
        } else {
            selectedTags.delete(tagValue);
        }
    }

    // 동적으로 생성된 태그 옵션 버튼에 클릭 이벤트를 바인딩
    $(document).on('click', '.tag-button', function (event) {
        event.preventDefault();
        toggleTagSelection($(this));
    });

    // 사용자 정의 태그 및 추천 태그 표시
    function populateTags(userDefinedTags, recommendedTags) {
        const $userDefinedTagList = $('#user-defined-tag-list');
        const $recommendedTagList = $('#recommended-tag-list');

        $userDefinedTagList.empty(); // 초기화
        $recommendedTagList.empty(); // 초기화

        if (userDefinedTags) {
            $.each(userDefinedTags, function(_, tag) {
                $userDefinedTagList.append(createTagElement(tag.id, tag.name, 'user'));
            });
        }
        if (recommendedTags) {
            $.each(recommendedTags, function(_, tag) {
                $recommendedTagList.append(createTagElement(tag.id, tag.name, 'recommended'));
            });
        }
    }

    // 서브 카테고리 채우기
    function populateSubCategories(mainCategory, selectedSubCategory = '') {
        const $subCategorySelect = $('#sub-category');
        $subCategorySelect.empty().append('<option value="">Select Sub Category</option>'); // 초기화

        const categories = subCategories[mainCategory] || [];

        $.each(categories, function(_, subCategory) {
            const $option = $('<option>', { value: subCategory.value, text: subCategory.name });
            if (selectedSubCategory === subCategory.value) {
                $option.prop('selected', true);
            }
            $subCategorySelect.append($option);
        });
    }

    // 태그 요소 표시 CSS
    function createTagElement(tagId, tagName, tagType) {
        const $li = $('<li>', { class: 'tag-item' }).html(
            `${tagName} <button class='tag-del-btn' data-id='${tagId}' data-type='${tagType}'>x</button>`
        );

        $li.find('.tag-del-btn').on('click', function() {
            deleteTag(tagId, tagType);
        });

        return $li;
    }

    // 태그 삭제 요청
    function deleteTag(tagId, tagType) {
        let accessToken = localStorage.getItem('accessToken');
        accessToken = accessToken ? accessToken.trim() : '';
        const url = tagType === 'user' ? `/api/items/userTag/${tagId}` : `/api/items/recommendedTag/${tagId}`;

        $.ajax({
            url: url,
            method: 'DELETE',
            headers: {
                'Authorization': accessToken,
                'Content-Type': 'application/json'
            },
            success: function() {
                $(`button[data-id='${tagId}']`).parent().remove(); // UI에서 제거
            },
            error: function(xhr) {
                console.error('Error deleting tag:', xhr.status);
            }
        });
    }

    // 이미지 미리보기와 삭제 버튼 추가
    function populateImages(imgList) {
        const $imageContainer = $('#image-container');
        $imageContainer.empty(); // 초기화

        $.each(imgList, function(_, imgUrl) {
            const $imgWrapper = $('<div>', { class: 'image-wrapper' });
            const $img = $('<img>', { src: imgUrl, class: 'image-preview' });
            const $deleteBtn = $('<span>', { text: 'x', class: 'del-btn' });

            $deleteBtn.on('click', function() {
                deleteImage(imgUrl);
            });

            $imgWrapper.append($img).append($deleteBtn);
            $imageContainer.append($imgWrapper);
        });
    }

    // 이미지 삭제 요청
    function deleteImage(imgUrl) {
        let accessToken = localStorage.getItem('accessToken');
        accessToken = accessToken ? accessToken.trim() : '';

        $.ajax({
            url: `/api/items/img?img=${encodeURIComponent(imgUrl)}`,
            method: 'DELETE',
            headers: {
                'Authorization': accessToken,
                'Content-Type': 'application/json'
            },
            success: function() {
                $(`img[src='${imgUrl}']`).parent().remove(); // UI에서 이미지 제거
            },
            error: function(xhr) {
                console.error('Error deleting image:', xhr.status);
            }
        });
    }

    // 태그를 추가한다
    function addTag(value) {
        hashtag[counter] = value; // 태그를 Object 안에 추가
        counter++; // counter 증가, 삭제를 위한 del-btn의 고유 id가 된다
    }

    // 최종적으로 서버에 넘길 때 tag 안에 있는 값을 array type으로 만들어서 넘긴다
    function marginTag() {
        return Object.values(hashtag)
            .filter(word => word !== "");
    }

    // 태그 추가 > 엔터 입력 시 태그를 추가된다
    function handleTagInput(e) {
        var self = $(this);
        var tagValue = self.val().trim(); // 입력값을 가져오고, 앞뒤 공백 제거

        // 엔터 키 입력 시 동작
        if (e.key === "Enter" || e.keyCode === 13) {
            // 값이 비어있지 않은 경우
            if (tagValue !== "") {
                // 같은 태그가 있는지 검사
                if (!Object.values(hashtag).includes(tagValue)) {
                    $("#tag-list").append(
                        `<li class='tag-item'>${tagValue}<button class='tag-del-btn' idx='${counter}'>x</button></li>`
                    );
                    addTag(tagValue);
                    self.val("");
                } else {
                    alert("태그값이 중복됩니다.");
                }
            }
            e.preventDefault(); // 엔터 키의 기본 동작 방지
        }
    }

    // 태그 선택 및 취소 기능 추가
    $(document).on("click", ".tag-button", function () {
        const button = $(this);
        const tagValue = button.text().trim();

        // 태그 버튼 클릭 시 선택/해제 토글
        if (button.hasClass('selected')) {
            button.removeClass('selected');
            selectedTags.delete(tagValue); // 선택 해제
        } else {
            button.addClass('selected');
            selectedTags.add(tagValue); // 선택 추가
        }
    });

    // 삭제 버튼 클릭 시 태그 삭제
    $(document).on("click", ".tag-del-btn", function () {
        var index = $(this).attr("idx");
        delete hashtag[index]; // 해시태그 객체에서 삭제
        $(this).parent().remove(); // 리스트에서 삭제
    });

    // 이미지 파일 선택 및 미리보기 표시
    function handleFilePreview(event) {
        const files = event.target.files;
        const previewContainer = $('#image-preview-container');

        // 기존 이미지 파일 목록에 새로 선택된 파일을 추가
        Array.from(files).forEach(file => {
            if (!existingFiles.some(existingFile => existingFile.name === file.name && existingFile.size === file.size)) {
                existingFiles.push(file);
                const reader = new FileReader();

                reader.onload = function(e) {
                    const img = $('<img>').attr('src', e.target.result).addClass('image-preview');
                    const deleteBtn = $('<span>').text('x').addClass('del-btn');

                    deleteBtn.on('click', function () {
                        const index = existingFiles.findIndex(f => f.name === file.name && f.size === file.size);
                        if (index > -1) {
                            existingFiles.splice(index, 1);
                        }
                        img.parent().remove();
                    });

                    const container = $('<div>').addClass('image-preview-container').append(img).append(deleteBtn);
                    previewContainer.append(container);
                };

                reader.readAsDataURL(file);
            }
        });
    }

    $('#productImage').on('change', handleFilePreview);

    // 상품 등록 시 이미지와 다른 데이터 전송
    $('#submit-button').on('click', function () {
        const formData = new FormData();

        // 아이템 업데이트 정보
        const itemUpdate = JSON.stringify({
            nameKor: $('#productName').val(),
            price: $('#productPrice').val(),
            description: $('#productDescription').val(),
            sanrioCharacters: $('#productCategory').val(),
            mainCategory: $('#main-category').val(),
            subCategory: $('#sub-category').val(),
            userDefinedTagNames: marginTag(),
            recommendedTagOptions: Array.from(selectedTags) // selectedTags는 Set 객체로 변환하여 사용
        });

        formData.append('itemUpdate', itemUpdate);

        // 파일을 FormData에 추가
        if (existingFiles.length > 0) {  // 파일이 있을 때만 추가
            existingFiles.forEach(file => {
                formData.append('productImage', file);
            });
        }

        // 상품 등록 요청
        let accessToken = localStorage.getItem('accessToken');
        accessToken = accessToken ? accessToken.trim() : '';
        const id = localStorage.getItem('selectedItemId');

        if (accessToken) {
            fetch('/api/items/' + id, {
                method: 'PATCH',
                body: formData,
                headers: {
                    'Authorization': accessToken // 커스텀 헤더
                }
            })
            .then(response => {
                if (!response.ok) {
                    return response.text().then(text => {
                        throw new Error(`HTTP error! Status: ${response.status}. Response: ${text}`);
                    });
                } else {
                    console.log('상품이 수정되었습니다');
                    alert('상품이 수정되었습니다');
                    window.location.href = '/view/sale';
                }
            })
            .catch(error => {
                console.error('상품 수정요청을 처리하는 과정에서 오류가 발생했습니다.:', error);
            });
        } else {
            console.error('액세스 토큰에 대한 입력이 없습니다.');
        }
    });
});