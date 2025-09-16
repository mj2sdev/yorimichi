# Git 규칙에 대하여

이 페이지에서는 git, GitHub 관련 사용법, 전략을 설명합니다.
효율적인 개발을 위해 아래 규칙을 따라 개발을 진행하여 주시기 바랍니다.
해당 문서는 누구든 자유롭게 추가 가능하며 공공의 이익을 위해 부족한 내용이 존재한다면 추가해 주시기를 바랍니다.

## 📚 목차
- [깃 명령어 모음](#📦-깃-명령어-모음)
- [사용할 깃 전략](#👨‍🎨-사용할-깃-전략)

## 📦 깃 명령어 모음

> [!tip]  
> Git이란? 개발 중에 진척도 저장, 되돌아가기, 공간나누기를 효율적으로 도와주는 개발 도구입니다.

### 💻 책상에 앉으면 우선
```bash
# 깃허브에 올라온 모든 것들을 받기
git pull
```

### 👀 상태 확인
```bash
#현재 상태 확인 (내가 뭘 망쳤는지 보기)
git status
```

### ✏️ 내가 작업한 것 기록하기
```bash
# 파일 스테이징 (변경사항 올리기)
git add <파일명>		# 특정 파일
git add .			# 전부 다 올리기
```

```bash
# 커밋 (변경사항 저장)
# ["메시지" 작성에 관한 규칙이 있으니 확인 바랍니다.]
git commit -m "메시지"
```

### 🪾 브랜치 (작업 공간 나누기)
> [!tip]
> 브랜치란? 개발 중 완성본은 놔두고 새로 복사해서 이것저것 추가하고 싶을 때 만드는 새로운 작업공간

```bash
# 브랜치 목록 확인
git branch
```

```bash
# 브랜치 생성
git branch feature/login
```

```bash
# 브랜치 이동
git checkout feature/login
```

```bash
# 브랜치 생성 + 이동 (원큐에)
git checkout -b feature/login
```

### ☁️ 원격 저장소 (GitHub)

> [!tip]
> GitHub이란? 개발 중 사용한 Git 의 기록을 공유할 수 있도록 도와주는 서비스 입니다. 

```bash
# 원격 저장소 추가
git remote add origin <URL>
```

```bash
# 원격 저장소 확인
git remote -v
```

```bash
# push (코드 밀어넣기)
git push origin main
git push -u origin feature/login
```

### 💩 옛날 만든 브랜치, <br> 최신 기준으로 하고싶어!

```bash
# 내 브랜치를 main 기준으로 재정렬
git checkout feature/login
git fetch origin
git rebase origin/main
```

### ⏪ 작업 취소, 수정, 돌아가기
```bash
# 스테이징 취소
git reset <파일명>
```

```bash
# 마지막 커밋 메시지 수정
git commit --amend
```

```bash
# 과거 커밋으로 순간이동 (주의: 과거로 간다)
git checkout <커밋ID>
```

### 🚕 작업중에 다른 브랜치 구경갈 때, <br> 지금까지 해놨던 내용 저장

```bash
# 변경사항을 스택에 저장 (기본)
git stash
```
```bash
# 메시지 달아서 저장 (여러 개 관리할 때 유용)
git stash save "작업중: 로그인 기능"
```
```bash
# stash 목록 보기
git stash list
```
```bash
# 최근 stash 불러오기 (적용)
git stash apply
```
```bash
# 특정 stash 불러오기
git stash apply stash@{2}
```
```bash
# stash 적용 + 목록에서 제거
git stash pop
```
```bash
# 특정 stash 제거
git stash drop stash@{0}
```
```bash
# stash 전부 날리기
git stash clear
```

# 👨‍🎨 사용할 깃 전략
## **(GitFlow)**
> [!chat gpt 😎]
> GitFlow는 브랜치라는 작업 공간을 정리정돈 끝판왕으로 관리하게 해주는 전략이야.  
처음 보면 좀 헷갈리지만, 한 번 이해하면 질질 쌀 정도로 편해진다.

### 🗺️ 흐름도
```
               hotfix/* o⎯o
                        ↑  ↓
main ⎯⎯⎯⎯⎯⎯⎯⎯o⎯o⎯o⎯⎯>  
  ↓                  ↑
develop ⎯⎯⎯o⎯⎯⎯o⎯⎯⎯⎯⎯>
  ↓       	  ↑      ↓
feature/* ⎯⎯o      o⎯⎯⎯>
```
각 항목:
- main: 완성된 프로그램 (안정버전)
- develop: 개발중인 프로그램 (변경 취합, 테스트 진행)
- feature: 새로운 기능 개발
- hotfix: 버그, 오류 긴급수정


# 한마디
코드는 순간이지만 기록은 영원하다.  
Git은 우리의 타임머신이자, 맛있는 개발자의 언어다. 