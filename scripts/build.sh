#!/usr/bin/env bash

set -o errexit

function print_help {
  echo "usage: $0 [-o now|next | -v, --verbose | -h, --help] path"
}

OM=false
NEXT=false

while [[ $# > 0 ]]
do
case $1 in
    -v|--verbose)
    VERBOSE=true
    shift
    ;;
    -h|--help)
    print_help
    exit 0
    ;;
    -o|-om)
    shift
    if [[ $1 == "now" ]]; then
      OM=true
    elif [[ $1 == "next" ]]; then
      NEXT=true
    fi
    shift
    break
    ;;
    --) # end of all options
    shift
    break
    ;;
    -*) # unknown option
    echo "Error: Unknown option: $1" >&2
	  exit 1
    ;;
    *) # no more options
    break
    ;;
esac
done

function set_verbose_output {
  if [ $VERBOSE ]; then
    set -o xtrace
    set +o verbose
  fi
}

if [[ $OM = false ]] && [[ $NEXT = false ]]; then
  echo "-o must be set to \"now\" or \"next\""
  exit 1
fi

if [[ $# > 0 ]]; then
  DIR=$1
else
  echo "Error: no path specified"
  print_help
  exit 1
fi

set_verbose_output

MASTER_DIR=checkout

GIT_REPO_HEAD_SHA=$(git rev-parse --short HEAD)
GIT_REPO_REMOTE_URL=$(git config --get remote.origin.url)
GIT_MASTER_BRANCH=master
GIT_DEPLOY_BRANCH=gh-pages
GIT_SUBTREE_OPTS="--git-dir=$DIR/.git --work-tree=$DIR"


rm -rf $DIR

if [[ $OM = true ]]; then
  LEIN_PROFILE="+om"
elif [[ $NEXT = true ]]; then
  LEIN_PROFILE="+next"
fi

git clone -b $GIT_MASTER_BRANCH --single-branch $GIT_REPO_REMOTE_URL $MASTER_DIR
pushd $MASTER_DIR

lein with-profile $LEIN_PROFILE clean
lein with-profile $LEIN_PROFILE cljsbuild once prod

mkdir -p $DIR
git clone -b $GIT_DEPLOY_BRANCH --single-branch $GIT_REPO_REMOTE_URL $DIR

rsync -av --exclude='/assets/js/out-adv/' resources/public/ $DIR

git $GIT_SUBTREE_OPTS add -A .
git $GIT_SUBTREE_OPTS commit -am "deploy release of ${GIT_REPO_HEAD_SHA}"
git $GIT_SUBTREE_OPTS push origin $GIT_DEPLOY_BRANCH

popd
# Cleanup
rm -rf $MASTER_DIR
