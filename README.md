# csa-web


git config --global user.email "eric.son04272006@gmail.com"
git config --global user.name "Eric Son"

# to check the current status
git status

# stage files to be commited
git add .

# commit the files to your local git
git commit -m 'some description'

# push your commits to github.com
git push

# get latest change into your local from github.com
git pull

# run this to compile your project using gradle
./gradlew clean build jar

# to run the execution jar
java -jar build/libs/csa-web-1.0.0.jar