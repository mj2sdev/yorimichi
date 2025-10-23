(function (){
	 const submitBtn = document.getElementById('myinfosubmit');
	 
	 submitBtn.addEventListener('click', async function() {
		const data = formData('myinfo-form');
		const response = await request('PUT', '/user/mypage', {body: data});
		if(response){
			window.location.reload();
		}else{
			alert('정보 수정 실패');
            window.location.reload();
		}
	});
})();

//좋아요
(function likeSwitch() {
    const likeSwitchBtns = document.querySelectorAll(".like-switch-btn");
    likeSwitchBtns.forEach(button => {
        button.addEventListener('click', async function() {
            const storeId = this.dataset.likeStoreid;
            const icon = this.querySelector('i');
            const isLiked = icon.classList.contains('bi-heart-fill');

            try {
                let result;
                if (isLiked) {
                    result = await request('DELETE', '/store/like/{storeId}', {params:{'storeId': storeId}});
                } else {
                    result = await request('POST', '/store/like/{storeId}', {params:{'storeId': storeId}});
                }

                if (isLiked) {
                    icon.classList.remove('bi-heart-fill');
                    icon.classList.add('bi-heart');
                } else {
                    icon.classList.remove('bi-heart');
                    icon.classList.add('bi-heart-fill');
                }

            } catch (error) {
                console.error("좋아요 처리 실패:", error);
                alert("요청 처리 중 오류가 발생했습니다.");
            }
        });
    });
})();

//북마크
(function bookmarkSwitch() {
    const bookmarkSwitchBtns = document.querySelectorAll(".bookmark-switch-btn");
    bookmarkSwitchBtns.forEach(button => {
        button.addEventListener('click', async function() {
            const storeId = this.dataset.bookmarkStoreid;
            const icon = this.querySelector('i');
            const isBookmarked = icon.classList.contains('bi-bookmark-fill');

            try {
                let result;
                if (isBookmarked) {
                    result = await request('DELETE', '/store/bookmark/{storeId}', { params: { 'storeId': storeId } });
                } else {
                    result = await request('POST', '/store/bookmark/{storeId}', { params: { 'storeId': storeId } });
                }

                if (isBookmarked) {
                    icon.classList.remove('bi-bookmark-fill');
                    icon.classList.add('bi-bookmark');
                } else {
                    icon.classList.remove('bi-bookmark');
                    icon.classList.add('bi-bookmark-fill');
                }
            } catch (error) {
                console.error("북마크 처리 실패:", error);
                alert("요청 처리 중 오류가 발생했습니다.");
            }
        });
    });
})();

//팔로우
(function followSwitch() {
    const followSwitchBtns = document.querySelectorAll(".follow-switch-btn");
    followSwitchBtns.forEach(button => {
        button.addEventListener('click', async function() {
            const userId = this.dataset.userId;
            const icon = this.querySelector('i');
            const isFollowing = icon.classList.contains('bi-heart-fill');

            try {
                let result;
                if (isFollowing) {
                    result = await request('DELETE', '/follow/{userId}', { params: { 'userId': userId } });
                } else {
                    result = await request('POST', '/follow/{userId}', { params: { 'userId': userId } });
                }

   
                    if (isFollowing) {
                        icon.classList.remove('bi-heart-fill');
                        icon.classList.add('bi-heart');
                    } else {
                        icon.classList.remove('bi-heart');
                        icon.classList.add('bi-heart-fill');
                    }

            } catch (error) {
                console.error("팔로우 처리 실패:", error);
                alert("요청 처리 중 오류가 발생했습니다.");
            }
        });
    });
})();

//차단
(function blockSwitch() {
    const blockSwitchBtns = document.querySelectorAll(".block-switch-btn");
    blockSwitchBtns.forEach(button => {
        button.addEventListener('click', async function() {
            const userId = this.dataset.userId;
            const icon = this.querySelector('i');
            const isBlocked = icon.classList.contains('bi-toggle-on');
            try {
                let result;
                if (isBlocked) {
                    result = await request('DELETE', '/block/{userId}', { params: { 'userId': userId } });
                } else {
                    result = await request('POST', '/block/{userId}', { params: { 'userId': userId } });
                }

                    if (isBlocked) {
                        icon.classList.remove('bi-toggle-on');
                        icon.classList.add('bi-toggle-off');
                    } else {
                        icon.classList.remove('bi-toggle-off');
                        icon.classList.add('bi-toggle-on');
                    }
                    
            } catch (error) {
                console.error("차단 처리 실패:", error);
                alert("요청 처리 중 오류가 발생했습니다.");
            }
        });
    });
})();