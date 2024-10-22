#!/bin/sh

# GitLab에서 pull
git pull origin master

# 모든 브랜치도 가져오기
git fetch --all

# 각 브랜치 동기화
for branch in $(git branch -r | grep -v HEAD); do
    git checkout ${branch#origin/}
    git pull origin ${branch#origin/}
    git push origin ${branch#origin/}
done

# master 브랜치로 돌아가기
git checkout master
