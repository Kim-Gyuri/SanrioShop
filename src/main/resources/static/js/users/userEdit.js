    // Handle logo click
    $('#logo-link').on('click', function(event) {
        event.preventDefault();
        window.location.href = '/view/home';
    });


    document.getElementById('uploadButton').addEventListener('click', function () {
        // 파일 선택 창 열기
        document.getElementById('profileImage').click();
    });



    $(document).ready(function () {

        let accessToken = localStorage.getItem('accessToken');
        accessToken = accessToken ? accessToken.trim() : '';

        // 유저 정보를 가져오기
        $.ajax({
            url: `/user`,
            method: 'GET',
            headers: { 'Authorization': accessToken },
            contentType: 'application/json',
            success: function (data) {
                populateForm(data); // 폼 데이터 채우기
            },
            error: function (xhr) {
                console.error('유저 정보를 불러오는데 오류가 발생했습니다.:', xhr.status);
            }
        });

        function populateForm(user) {
            $('#nickname').val(user.nickname);
            document.getElementById('profile-pic').style.backgroundImage = `url(${user.profileImg})`;
        }

         // 닉네임 변경 요청
        $('#updateNick').on('click', function () {
             const nickname = $('#nickname').val();

             $.ajax({
                url: '/user/nickname',
                method: 'PATCH',
                headers: { 'Authorization': accessToken },
                contentType: 'application/json',
                data: JSON.stringify({ nickname: nickname }),
                success: function () {
                    console.log('닉네임이 성공적으로 업데이트되었습니다.');
                    alert('변경되었습니다.');
                },
                error: function (xhr) {
                    alert('이미 사용중인 닉네임입니다.');
                    console.error('이미 사용중인 닉네임입니다.:', xhr.status);
                }
             });
        });


        // 프로필 변경 요청
        document.getElementById('profileImage').addEventListener('change', function (event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    // 미리보기 설정
                    document.getElementById('profile-pic').style.backgroundImage = `url(${e.target.result})`;
                };
                reader.readAsDataURL(file);

                // 프로필 변경 요청
                const formData = new FormData();
                formData.append('file', file);


                $.ajax({
                    url: '/user/profile',
                    method: 'PATCH',
                    headers: { 'Authorization': accessToken },
                    processData: false,
                    contentType: false,
                    data: formData,
                    success: function () {
                        console.log('프로필 이미지가 성공적으로 업데이트되었습니다.');
                        alert('변경되었습니다.');
                    },
                    error: function (xhr) {
                        console.error('프로필 변경 요청을 실패했습니다.:', xhr.status);
                        alert('프로필 수정 중에 오류가 발생했습니다.');
                    }
                });
            }
        });
    });