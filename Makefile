MVN=mvn
SHELL=bash
M2_REPO=~/.m2
#LZ4_VERSION=$(shell $(MVN) -o dependency:list | grep lz4 | cut -d ":" -f 4)
#RHINO_VERSION=$(shell $(MVN) -o dependency:list | grep rhino | cut -d ":" -f 4)
#MARKDOWNJ_VERSION=$(shell $(MVN) -o dependency:list | grep markdownj-core | cut -d ":" -f 4)
LZ4_VERSION=$(shell grep "<lz4\.version>" pom.xml | cut -d ">" -f 2 | cut -d "<" -f 1)
RHINO_VERSION=$(shell grep "<rhino\.version>" pom.xml | cut -d ">" -f 2 | cut -d "<" -f 1)
MARKDOWNJ_VERSION=$(shell grep "<markdownj\.version>" pom.xml | cut -d ">" -f 2 | cut -d "<" -f 1)
GITVERSION_FILE=src/main/resources/trafimage-geoserver-transformations.gitversion

build: git-version readme
	rm -f target/*.jar target/*.zip
	$(MVN) -Dmaven.test.skip=true package
	@# dependencies which are not already bundled in geoserver
	cp $(M2_REPO)/repository/net/jpountz/lz4/lz4/$(LZ4_VERSION)/lz4-$(LZ4_VERSION).jar target/
	cp $(M2_REPO)/repository/org/markdownj/markdownj-core/$(MARKDOWNJ_VERSION)/markdownj-core-$(MARKDOWNJ_VERSION).jar target/
	cp $(M2_REPO)/repository/org/mozilla/rhino/$(RHINO_VERSION)/rhino-$(RHINO_VERSION).jar target/
	@# build a zip-file containing al jars
	cd target && zip all-jars.zip *.jar

package: build

readme:
	cp README.md src/main/resources/README.trafimage-geoserver-transformations.md

git-version:
	./print-git-commit-hash.sh >$(GITVERSION_FILE)
	@# update the copy of the file cached in mavens build directory
	([ -d target/classes/ ] && cp $(GITVERSION_FILE) target/classes/) || true

clean:
	rm -f $(GITVERSION_FILE) src/main/resources/README.trafimage-geoserver-transformations.md
	$(MVN) clean
