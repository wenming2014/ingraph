MATCH (:Person {id:30786325579101})-[path:KNOWS*1..3]-(friend:Person)
WHERE friend.firstName = 'Ian'
WITH friend, min(length(path)) AS distance
ORDER BY distance ASC, friend.lastName ASC, toInt(friend.id) ASC
LIMIT 20
MATCH (friend)-[:IS_LOCATED_IN]->(friendCity:Place)
OPTIONAL MATCH (friend)-[studyAt:STUDY_AT]->(uni:Organisation)-[:IS_LOCATED_IN]->(uniCity:Place)
WITH
  friend,
  collect(
    CASE uni.name
      WHEN null THEN null
      ELSE [uni.name, toString(studyAt.classYear), uniCity.name]
    END
  ) AS unis,
  friendCity,
  distance
OPTIONAL MATCH (friend)-[workAt:WORK_AT]->(company:Organisation)-[:IS_LOCATED_IN]->(companyCountry:Place)
WITH
  friend,
  collect(
    CASE company.name
      WHEN null THEN null
      ELSE [company.name, toString(workAt.workFrom), companyCountry.name]
    END
  ) AS companies,
  unis,
  friendCity,
  distance
RETURN
  friend.id AS friendId,
  friend.lastName AS friendLastName,
  distance AS distanceFromPerson,
  friend.birthday AS friendBirthday,
  friend.creationDate AS friendCreationDate,
  friend.gender AS friendGender,
  friend.browserUsed AS friendBrowserUsed,
  friend.locationIP AS friendLocationIp,
  //friend.email AS friendEmails,
  //friend.speaks AS friendLanguages,
  friendCity.name AS friendCityName,
  unis AS friendUniversities,
  companies AS friendCompanies
ORDER BY distanceFromPerson ASC, friendLastName ASC, toInteger(friendId) ASC
LIMIT 20
