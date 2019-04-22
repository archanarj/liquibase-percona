import java.sql.ResultSet;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

File buildLog = new File( basedir, 'build.log' )
assert buildLog.exists()
def buildLogText = buildLog.text;
def defaultOptions = "--alter-foreign-keys-method=auto --no-check-unique-key-change --no-check-alter"
assert buildLogText.contains("liquibase: test-changelog.xml: test-changelog.xml::2::Alice: Executing: pt-online-schema-change ${defaultOptions} --alter=\"ADD COLUMN age INT NULL\" --host=${config_host} --port=${config_port} --user=${config_user} --password=*** --execute D=testdb,t=person")
assert buildLogText.contains("liquibase: test-changelog.xml: test-changelog.xml::3::Alice: Executing: pt-online-schema-change ${defaultOptions} --alter=\"DROP COLUMN age\"")
assert buildLogText.contains("liquibase: test-changelog.xml: test-changelog.xml::4::Alice: Executing: pt-online-schema-change ${defaultOptions} --alter=\"ADD UNIQUE INDEX emailIdx (email)\"")
assert buildLogText.contains("liquibase: test-changelog.xml: test-changelog.xml::5::Alice: Executing: pt-online-schema-change ${defaultOptions} --alter=\"DROP INDEX emailIdx\"")
assert buildLogText.contains("liquibase: test-changelog.xml: test-changelog.xml::6::Alice: Executing: pt-online-schema-change ${defaultOptions} --alter=\"MODIFY email VARCHAR(400)\"")
assert buildLogText.contains("liquibase: test-changelog.xml: test-changelog.xml::8::Alice: Executing: pt-online-schema-change ${defaultOptions} --alter=\"ADD CONSTRAINT fk_person_address FOREIGN KEY (person_id) REFERENCES person (name)\"")
assert buildLogText.contains("liquibase: test-changelog.xml: test-changelog.xml::9::Alice: Executing: pt-online-schema-change ${defaultOptions} --alter=\"DROP FOREIGN KEY _fk_person_address\"")
assert buildLogText.contains("liquibase: test-changelog.xml: test-changelog.xml::11::Alice: Executing: pt-online-schema-change ${defaultOptions} --alter=\"DROP PRIMARY KEY, ADD PRIMARY KEY (id, name)\"")

File sql = new File( basedir, 'target/liquibase/migrate.sql' )
assert sql.exists()
def sqlText = sql.text;
assert sqlText.contains("pt-online-schema-change ${defaultOptions} --alter=\"ADD COLUMN age INT NULL\"")
assert sqlText.contains("pt-online-schema-change ${defaultOptions} --alter=\"DROP COLUMN age\"")
assert sqlText.contains("pt-online-schema-change ${defaultOptions} --alter=\"ADD UNIQUE INDEX emailIdx (email)\"")
assert sqlText.contains("pt-online-schema-change ${defaultOptions} --alter=\"DROP INDEX emailIdx\"")
assert sqlText.contains("pt-online-schema-change ${defaultOptions} --alter=\"MODIFY email VARCHAR(400)\"")
assert sqlText.contains("pt-online-schema-change ${defaultOptions} --alter=\"ADD CONSTRAINT fk_person_address FOREIGN KEY (person_id) REFERENCES person (name)\"")
assert sqlText.contains("pt-online-schema-change ${defaultOptions} --alter=\"DROP FOREIGN KEY _fk_person_address\"")
// Note: only adding the primary key, not dropping it in the migration sql.
assert sqlText.contains("pt-online-schema-change ${defaultOptions} --alter=\"ADD PRIMARY KEY (id, name)\"")
assert !sqlText.contains("password=${config_password}")
