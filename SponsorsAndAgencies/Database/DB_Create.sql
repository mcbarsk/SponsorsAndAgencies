CREATE TABLE `agencies` (
  `idAgencies` int(11) NOT NULL AUTO_INCREMENT,
  `worldID` varchar(45) NOT NULL,
  `creationDate` datetime NOT NULL,
  `name` int(11) DEFAULT '0',
  `chosenSponsor` int(11) DEFAULT NULL,
  `budget` double DEFAULT '0',
  `moneyNeeded` double DEFAULT '0',
  `savings` double DEFAULT '0',
  `risk` double DEFAULT '0',
  `position_x` double DEFAULT '0',
  `position_y` double DEFAULT '0',
  `payout` double DEFAULT '0',
  `eyesight` double DEFAULT '0',
  `cutdown` int(11) DEFAULT '0',
  `iteration` int(11) DEFAULT NULL,
  PRIMARY KEY (`idAgencies`),
  KEY `idx_agencies_worldID_name` (`worldID`,`name`),
  KEY `idx_agencies_worldID_name_iteration` (`worldID`,`iteration`,`name`),
  KEY `idx_agencies_worldID` (`worldID`),
  KEY `idx_agencies_worldID_iteration` (`worldID`,`iteration`),
  KEY `idx_agencies_name_worldID` (`name`,`worldID`),
  KEY `idx_agencies_name_worldID_iteration` (`name`,`worldID`,`iteration`)
) ENGINE=InnoDB AUTO_INCREMENT=520480 DEFAULT CHARSET=utf8;

CREATE TABLE `sponsors` (
  `idsponsors` int(11) NOT NULL AUTO_INCREMENT,
  `worldID` varchar(45) DEFAULT NULL,
  `creationDate` datetime DEFAULT NULL,
  `name` int(11) DEFAULT NULL,
  `position_x` double DEFAULT NULL,
  `position_y` double DEFAULT NULL,
  `money` double DEFAULT NULL,
  `payoff` double DEFAULT NULL,
  `iteration` int(11) DEFAULT NULL,
  PRIMARY KEY (`idsponsors`),
  KEY `idx_sponsors_name_worldID` (`name`,`worldID`),
  KEY `idx_sponsors_worldID` (`worldID`)
) ENGINE=InnoDB AUTO_INCREMENT=105651 DEFAULT CHARSET=utf8;

CREATE TABLE `worlds` (
  `idworld` int(11) NOT NULL AUTO_INCREMENT,
  `worldID` varchar(45) NOT NULL,
  `creationDate` datetime NOT NULL,
  `initialNumberOfSponsors` int(11) DEFAULT '0',
  `initialNumberOfAgencies` int(11) DEFAULT NULL,
  `moveSetting` varchar(45) NOT NULL,
  `cutDownModel` varchar(45) NOT NULL,
  `worldSize` varchar(45) NOT NULL,
  `sponsorSigmaFactor` double DEFAULT '0',
  `sponsorMoney` double DEFAULT '0',
  `agencyMoney` double DEFAULT '0',
  `agencyMoneyReserveFactor` int(11) DEFAULT '0',
  `agencySigmaFactor` double DEFAULT '0',
  `agencyRequirementNeed` double DEFAULT '0',
  `agencyRequirementSigma` double DEFAULT '0',
  `sightOfAgency` double DEFAULT '0',
  `moveRate` double DEFAULT '0',
  `pickRandomSponsor` int(11) DEFAULT '0',
  `numberOfIterations` int(11) DEFAULT '0',
  PRIMARY KEY (`idworld`)
) ENGINE=InnoDB AUTO_INCREMENT=517673 DEFAULT CHARSET=utf8;


