CREATE TABLE `agencies` (
  `idAgencies` int(11) NOT NULL AUTO_INCREMENT,
  `worldID` varchar(45) NOT NULL,
  `creationDate` datetime NOT NULL,
  `name` int(11) DEFAULT '0',
  `risk` double DEFAULT '0',
  `eyesight` double DEFAULT '0',
  PRIMARY KEY (`idAgencies`),
  KEY `idx_agencies_worldID_name` (`worldID`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `agency_iterations` (
  `idagency_iteration` int(11) NOT NULL AUTO_INCREMENT,
  `worldID` varchar(45) NOT NULL,
  `name` int(11) NOT NULL,
  `chosenSponsor` int(11) DEFAULT NULL,
  `budget` double DEFAULT NULL,
  `moneyNeeded` double DEFAULT NULL,
  `savings` double DEFAULT NULL,
  `position_x` double DEFAULT NULL,
  `position_y` double DEFAULT NULL,
  `payout` double DEFAULT NULL,
  `cutdown` int(11) DEFAULT NULL,
  `iteration` int(11) NOT NULL,
  `percentageCut` double DEFAULT '0',
  PRIMARY KEY (`idagency_iteration`),
  UNIQUE KEY `idx_agency_iterations_worldID_iteration_name` (`worldID`,`iteration`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `sponsor_iterations` (
  `idsponsor_iterations` int(11) NOT NULL AUTO_INCREMENT,
  `worldID` varchar(45) NOT NULL,
  `name` int(11) NOT NULL,
  `payoff` double DEFAULT NULL,
  `iteration` int(11) NOT NULL,
  PRIMARY KEY (`idsponsor_iterations`),
  UNIQUE KEY `idx_sponsor_iterations_worldID_name_iteration` (`worldID`,`name`,`iteration`),
  KEY `idx_sponsor_iterations_worldID_name` (`worldID`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=196905 DEFAULT CHARSET=utf8;

CREATE TABLE `sponsors` (
  `idsponsors` int(11) NOT NULL AUTO_INCREMENT,
  `worldID` varchar(45) NOT NULL,
  `creationDate` datetime NOT NULL,
  `name` int(11) NOT NULL,
  `position_x` double DEFAULT NULL,
  `position_y` double DEFAULT NULL,
  `money` double DEFAULT NULL,
  PRIMARY KEY (`idsponsors`),
  UNIQUE KEY `idx_sponsors_worldID_name` (`worldID`,`name`),
  KEY `idx_sponsors_worldID` (`worldID`)
) ENGINE=InnoDB AUTO_INCREMENT=801 DEFAULT CHARSET=utf8;

CREATE TABLE `worlds` (
  `idworld` int(11) NOT NULL AUTO_INCREMENT,
  `worldID` varchar(45) NOT NULL,
  `worldname` varchar(45) DEFAULT NULL,
  `creationDate` datetime NOT NULL,
  `initialNumberOfSponsors` int(11) DEFAULT '0',
  `initialNumberOfAgencies` int(11) DEFAULT NULL,
  `cutDownModel` varchar(45) NOT NULL,
  `allocationMethod` varchar(45) NOT NULL,
  `worldSize` varchar(45) NOT NULL,
  `sponsorSigmaFactor` double DEFAULT '0',
  `sponsorMoney` double DEFAULT '0',
  `respectSponsorMoney` int(11) DEFAULT '0',
  `agencyMoney` double DEFAULT '0',
  `agencyMoneyReserveFactor` int(11) DEFAULT '0',
  `agencySigmaFactor` double DEFAULT '0',
  `agencyRequirementNeed` double DEFAULT '0',
  `agencyRequirementSigma` double DEFAULT '0',
  `sightOfAgency` double DEFAULT '0',
  `moveRate` double DEFAULT '0',
  `moveMethod` varchar(45) DEFAULT NULL,
  `numberOfIterations` int(11) DEFAULT '0',
  `baseRisk` double DEFAULT '0',
  `b0` double DEFAULT '0',
  `b1` double DEFAULT '0',
  `StatMean` double DEFAULT NULL,
  `StatLCV` double DEFAULT NULL,
  `StatSkewness` double DEFAULT NULL,
  `StatKurtosis` double DEFAULT NULL,
  `StatLLCV` double DEFAULT NULL,
  `StatLSkewness` double DEFAULT NULL,
  `StatLKurtosis` double DEFAULT NULL,
  PRIMARY KEY (`idworld`),
  KEY `idx_worlds_worldID` (`worldID`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;


