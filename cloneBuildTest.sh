# $1 is unique directory ID
# $2 is git clone URL
# $3 is git repository name
# $4 is git branch name
if ! cd work; then
  mkdir work
  cd work || exit
fi
mkdir temp"$1"
cd temp"$1" || exit
git clone "$2"
cd "$3" || exit
git checkout "$4"
mvn test
