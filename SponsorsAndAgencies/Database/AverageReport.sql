SELECT 
    a.`iteration`,
    COUNT(a.name) AS 'Number of agencies',
    AVG(a.`budget`) AS 'Average budget',
    AVG(a.`moneyNeeded`) AS 'Average money needed',
    AVG(a.`savings`) AS 'Average savings',
    AVG(a.`payout`) AS 'Average payout',
    COUNT(IF(a.`cutdown` = 1, 1, NULL)) AS 'Number cut',
    COUNT(IF(a.`cutdown` = 0, 0, NULL)) AS 'Number total payout',
    AVG(a.`percentageCut`) AS 'Average percentage cut',
    MAX(a.name),
    CASE
        WHEN a.iteration = 1 THEN 0
        ELSE MAX(a.name) - (SELECT 
                MAX(b.name)
            FROM
                `sponsors_agencies`.`agency_iterations` b
            WHERE
                a.worldID = b.worldID
                    AND b.iteration = a.iteration - 1)
    END AS 'New agencies'
FROM
    `sponsors_agencies`.`agency_iterations` a
WHERE
    a.worldID = '86179b3f-e822-4704-b4c9-7374294670a4'
GROUP BY a.iteration
