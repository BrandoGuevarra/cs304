drop table Player_Purchase_Champion;
drop table Purchase_And_Upgrade;
drop table Item_upgradeInto_Item;
drop table Champion_Wield_Item;
drop table Champion_Skills1;
drop table Champion_Skills2;
drop table Champion_Stats1;
drop table Champion_Stats2;
drop table Report_A_Player;
drop table Champion;
drop table Item;
drop table Player;

CREATE TABLE Player(
	Username varchar(15),
	Region varchar(10),
	Password varchar(15),
	playerLevel INTEGER,
	timesReported INTEGER,
	riotPoints INTEGER,
	ipPoints INTEGER,
	Division varchar(10),
	accountStatus varchar(10),
	PRIMARY KEY(Username, Region)
	);

ALTER TABLE Player
ADD CHECK (playerLevel > 0);

ALTER TABLE Player
ADD CHECK (ipPoints > 0);

grant select on Player to public;


CREATE TABLE Champion(
	Name varchar(30),
	championLevel INTEGER,
	ipCost INTEGER,
	Role varchar(32),
	passiveSkill varchar(50),
	Faction varchar(64),
	skillPoints INTEGER,
	PRIMARY KEY (Name));

grant select on Champion to public;

CREATE TABLE Player_Purchase_Champion(
	playerID varchar(15),
	playerRegion varchar(10),
	championID varchar(30),
	Cost INTEGER,
	purchasedWith varchar(2),
	PRIMARY KEY(playerID, playerRegion, championID),
	FOREIGN KEY (playerID, playerRegion) REFERENCES Player(Username, Region),
	FOREIGN KEY (championID) REFERENCES Champion(Name) on delete CASCADE );

grant select on Player_Purchase_Champion to public;


CREATE TABLE Item(
	Name varchar(30),
	Cost INTEGER,
	upgradePoints varchar(50),
	activeAbility varchar(50),
	Type varchar(15),
	PRIMARY KEY(Name));

grant select on Item to public;

CREATE TABLE Purchase_And_Upgrade(
	playerID varchar(15),
	playerRegion varchar(10),
	itemID varchar(30),
	Cost INTEGER,
	upgradeConversion varchar(32),
	purchasedWith varchar(2),
	PRIMARY KEY(playerID, playerRegion, itemID),
	FOREIGN KEY (playerID, playerRegion) REFERENCES Player(Username, Region),
	FOREIGN KEY (itemID) REFERENCES Item(Name));

grant select on Purchase_And_Upgrade to public;

CREATE TABLE Item_upgradeInto_Item(
	itemID1 varchar(30),
	itemID2 varchar(30),
	upgradePointsRequired INTEGER,
	PRIMARY KEY(itemID1, itemID2));

grant select on Item_upgradeInto_Item to public;

CREATE TABLE Champion_Wield_Item(
	championID varchar(15),
	itemID varchar(30),
	PRIMARY KEY(championID, itemID),
	FOREIGN KEY (championID) REFERENCES Champion(Name) ON DELETE CASCADE,
	FOREIGN KEY (itemID) REFERENCES Item(Name)
	ON DELETE CASCADE);

grant select on Champion_Wield_Item to public;

CREATE TABLE Champion_Skills1(
	championID varchar(30),
	Type varchar(15),
	Description varchar(256),
	skillLevel INTEGER,
	Effect varchar(200),
	PRIMARY KEY(championID, Type),
	FOREIGN KEY (championID) REFERENCES Champion(Name) on delete cascade);

grant select on Champion_Skills1 to public;

CREATE TABLE Champion_Skills2(
	championID varchar(30),
	skillLevel INTEGER,
	Damage INTEGER,
	Duration INTEGER,
	Cooldown INTEGER,
	PRIMARY KEY(championID, skillLevel),
	FOREIGN KEY(championID) REFERENCES Champion(Name) on delete cascade);

grant select on Champion_Skills2 to public;

CREATE TABLE Champion_Stats1(
	championID varchar(30),
	Type varchar(15),
	baseValue INTEGER,
	championLevel INTEGER,
	growthPerLevel INTEGER,
	PRIMARY KEY(championID, Type),
	FOREIGN KEY (championID) REFERENCES Champion(Name) on delete cascade);

grant select on Champion_Stats1 to public;

CREATE TABLE Champion_Stats2(
	championID varchar(30),
	champLevel INTEGER,
	growthPerLevel INTEGER,
	currentValue INTEGER,
	PRIMARY KEY(championID, champLevel, growthPerLevel),
	FOREIGN KEY (championID) REFERENCES Champion(Name) on delete CASCADE );

grant select on Champion_Stats2 to public;

CREATE TABLE Report_A_Player(
	reportID varchar(20),
	reporteeID varchar(15) not null,
	reporteeRegion varchar(10) not null,
	reporterID varchar(15) not null,
	reporterRegion varchar(10) not null,
	reportedTimeDayMonthYear DOUBLE not null,
	offendingAction varchar(50) not null,
	PRIMARY KEY(reportID),
	FOREIGN KEY (reporteeID, reporteeRegion) REFERENCES Player(Username, Region),
	FOREIGN KEY (reporterID, reporterRegion) REFERENCES Player(Username, Region));

grant select on Report_A_Player to public;

insert into Player
values('LemonNation', 'NA', '123',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Hai', 'NA', '234',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Balls', 'NA', '345',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('SneakyGASM', 'NA', 'pinsandneedles',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Meateos', 'NA', 'cam shot in 5',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('mid fekar', 'NA', '321',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Hide on Bush', 'KR', '321',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('DoubleLift', 'NA', 'yourealltrash',
'30', '26', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Bjergsen', 'NA', '789',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Aphromoo', 'NA', 'supportsoeasy',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('BunnyFuFuu', 'NA', 'hooksfordays',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Froggen', 'NA', '987',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Hauntzer', 'NA', '876',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Huni', 'NA', '765',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Impact', 'NA', 'topdie',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Jensen', 'NA', 'bodythosefools',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Rush', 'KR', 'nocounterplay',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Bang', 'KR', 'sneakydaddy',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('PraY', 'KR', '320',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Smeb', 'KR', '420',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Crown', 'KR', '103',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Ssumday', 'KR', '842',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('G2 ZV3N', 'EU', '482',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('FNC Rekkles', 'EU', '123456',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('FNC Febiven', 'EU', 'notgrossgore',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('Kikis', 'EU', '239058203',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Player
values('EDG DEFT', 'KR', 'guunenda',
'30', '0', '900', '100000', 'Challenger', 'OK');

insert into Champion
values('Brand', '1', '4800', 'Mage', 'Blaze', 'Independent', '1');

insert into Champion
values('Braum', '1', '6300', 'Controller, Tank', 'Concussive Blows', 'Freljord, Avarosan', '1');

insert into Champion
values('Fiddlesticks', '1', '1350', 'Mage, Controller', 'Dread', 'Independent', '1');

insert into Champion
values('Fiora', '1', '4800', 'Fighter, Slayer', 'Duelists Dance', 'Demacia, House Laurent', '1');

insert into Champion
values('Kindred', '1', '6300', 'Marksman', 'Mark of the Kindred', 'Independent', '1');

insert into Champion
values('Lee Sin', '1', '4800', 'Fighter, Slayer', 'Flurry', 'Ionia', '1');

insert into Champion
values('Lulu', '1', '4800', 'Controller, Mage', 'Pix, Faerie Companion', 'Independent', '1');

insert into Champion
values('Lux', '1', '3150', 'Mage, Controller', 'Illumination', 'Demacia', '1');

insert into Champion
values('Malphite', '1', '1350', 'Tank, Fighter', 'Granite Shield', 'Independent', '1');

insert into Champion
values('Maokai', '1', '4800', 'Tank, Mage', 'Sap Magic', 'Independent', '1');

insert into Champion
values('Nocturne', '1', '4800', 'Slayer, Fighter', 'Umbra Blades', 'Independent', '1');

insert into Champion
values('Olaf', '1', '3150', 'Fighter, Tank', 'Berserker Rage', 'Freljord, Winters Claw', '1');

insert into Champion
values('Orianna', '1', '4800', 'Mage, Controller', 'Clockwork Windup', 'Piltover', '1');

insert into Champion
values('Pantheon', '1', '3150', 'Fighter, Slayer', 'Aegis Protection', 'Mount Targon', '1');

insert into Champion
values('Quinn', '1', '6300', 'Marksman, Fighter', 'Harrier', 'Demacia', '1');

insert into Champion
values('Yasuo', '1', '6300', 'Fighter, Slayer', 'Way of the Wanderer', 'Independent', '1');

insert into Champion
values('Zac', '1', '6300', 'Tank, Fighter', 'Cell Division', 'Zaun', '1');

insert into Champion
values('Zed', '1', '6300', 'Slayer, Fighter', 'Contempt for the Weak', 'Ionia, The Order of the Shadow', '1');

insert into Champion
values('Ziggs', '1', '4800', 'Mage', 'Short Fuse', 'Bandle City, Piltover', '1');

insert into Champion
values('Zilean', '1', '1350', 'Controller, Mage', 'Time in a Bottle', 'Piltover', '1');

insert into Champion
values('Zyra', '1', '4800', 'Mage, Controller', 'Garden of Thorns', 'Independent', '1');

insert into Champion
values('Rammus', '1', '1350', 'Tank, Fighter', 'Spiked Shell', 'Independent', '1');

insert into Champion
values('RekSai', '1', '6300', 'Fighter', 'Fury of the XerSai', 'Independent', '1');

insert into Champion
values('Sejuani', '1', '4800', 'Tank, Fighter', 'Frost Armor', 'Freljord, Winters Claw', '1');

insert into Champion
values('Shen', '1', '3150', 'Tank', 'Ki Barrier', 'Ionia, Kinkou Order', '1');

insert into Champion
values('Shyvana', '1', '3150', 'Fighter, Tank', 'Dragonborn', 'Demacia', '1');

insert into Champion
values('Sion', '1', '1350', 'Tank, Fighter', 'Glory in Death', 'Noxus', '1');

insert into Champion
values('Sona', '1', '3150', 'Controller, Mage', 'Power Chord', 'Demacia, Ionia', '1');

insert into Champion
values('Swain', '1', '4800', 'Mage, Fighter', 'Carrion Renewal', 'Noxus, The Block Rose', '1');

insert into Champion
values('Syndra', '1', '6300', 'Mage, Controller', 'Transcendent', 'Independent', '1');

insert into Champion
values('Tahm Kench', '1', '6300', 'Controller, Tank', 'An Acquired Taste', 'Independent', '1');

insert into Champion
values('Taliyah', '1', '6300', 'Mage, Controller', 'Rock Surfing', 'Shurima', '1');

insert into Champion
values('Talon', '1', '4800', 'Slayer, Fighter', 'Mercy', 'Noxus', '1');

insert into Champion
values('Taric', '1', '1350', 'Controller, Fighter', 'Bravado', 'Mount Targon, Demacia', '1');

insert into Champion
values('Tryndamere', '1', '1350', 'Fighter, Slayer', 'Battle Fury', 'Freljord, Avarosan, Northern Barbarians', '1');

insert into Champion
values('Twisted Fate', '1', '1350', 'Mage', 'Loaded Dice', 'Independent', '1');

insert into Champion
values('Twitch', '1', '3150', 'Marksman, Slayer', 'Deadly Venom', 'Zaun', '1');

insert into Champion
values('Varus', '1', '6300', 'Marksman, Mage', 'Living Vengeance', 'Independent', '1');

insert into Champion
values('Vayne', '1', '4800', 'Marksman, Slayer', 'Night Hunter', 'Demacia', '1');

insert into Champion
values('Veigar', '1', '1350', 'Mage', 'Phenomenal Evil Power', 'Independent', '1');

insert into Champion
values('VelKoz', '1', '6300', 'Mage', 'Organic Deconstruction', 'The Void', '1');

insert into Champion
values('Vladimir', '1', '4800', 'Mage, Tank', 'Crimson Pact', 'Noxus, The Black Rose', '1');

insert into Champion
values('Warwick', '1', '450', 'Fighter, Tank', 'Eternal Thirst', 'Zaun', '1');

insert into Champion
values('Wukong', '1', '4800', 'Fighter, Tank', 'Stone Skin', 'Ionia', '1');

insert into Champion
values('Xerath', '1', '4800', 'Mage, Slayer', 'Mana Surge', 'Independent', '1');

insert into Champion
values('Xin Zhao', '1', '1350', 'Fighter, Slayer', 'Challenge', 'Demacia', '1');

insert into Champion
values('Nidalee', '1', '3150', 'Slayer, Fighter', 'Prowl', 'Independent', '1');

insert into Champion
values('Nautilus', '1', '4800', 'Tank, Fighter', 'Staggering Blow', 'Independent', '1');

insert into Champion
values('Nasus', '1', '1350', 'Fighter, Tank', 'Soul Eater', 'Shurima', '1');

insert into Champion
values('Nami', '1', '6300', 'Controller, Mage', 'Surging Tides', 'Guardians Sea, The Marai', '1');

insert into Champion
values('Morgana', '1', '1350', 'Mage, Controller', 'Soul Siphon', 'Noxus', '1');

insert into Champion
values('Miss Fortune', '1', '3150', 'Marksman', 'Love Tap', 'Bilgewater', '1');

insert into Champion
values('Leona', '1', '4800', 'Tank, Controller', 'Sunlight', 'Mount Targon, The Solari', '1');

insert into Champion
values('KhaZix', '1', '6300', 'Slayer, Fighter', 'Unseen Threat', 'The Void', '1');

insert into Champion
values('Kassadin', '1', '3150', 'Slayer, Mage', 'Void Stone', 'The Preservers of Valoran', '1');

insert into Champion
values('Kalista', '1', '6300', 'Marksman', 'Martial Poise', 'Shadow Isles', '1');

insert into Champion
values('Jarvan IV', '1', '4800', 'Tank, Fighter', 'Martial Cadence', 'Demacia', '1');

insert into Champion
values('Ivern', '1', '6300', 'Controller, Mage', 'Friend of the Forest', 'Independent', '1');

insert into Champion
values('Illaoi', '1', '6300', 'Fighter, Tank', 'Prophet of an Elder God', 'Bilgewater, Nagakabouros', '1');

insert into Champion
values('Heimerdinger', '1', '3150', 'Mage, Controller', 'Techmaturgical Repair Bots', 'Bandle City, Piltover', '1');

insert into Champion
values('Gragas', '1', '3150', 'Fighter, Mage', 'Happy Hour', 'Avarosan', '1');

insert into Champion
values('Gangplank', '1', '3150', 'Fighter', 'Trial by Fire', 'Bilgewater', '1');

insert into Champion
values('Fizz', '1', '4800', 'Slayer, Fighter', 'Nimble Fighter', 'Bilgewater', '1');

insert into Champion
values('Evelynn', '1', '1350', 'Slayer, Mage', 'Shadow Walk', 'Unknown', '1');

insert into Champion
values('Ekko', '1', '6300', 'Slayer, Fighter', 'Z-Drive Resonance', 'Zaun', '1');

insert into Champion
values('Darius', '1', '4800', 'Fighter, Tank', 'Hemorrhage', 'Noxus', '1');

insert into Champion
values('Draven', '1', '4800', 'Marksman', 'League of Draven', 'Noxus', '1');

insert into Champion
values('Corki', '1', '3150', 'Marksman', 'Hextech Munitions', 'Bandle City, Piltover', '1');

insert into Champion
values('Caitlyn', '1', '4800', 'Marksman', 'Headshot', 'Piltover', '1');

insert into Champion
values('Azir', '1', '6300', 'Mage, Marksman', 'Shurimas Legacy', 'Shurima', '1');

insert into Champion
values('Aurelion Sol', '1', '6300', 'Mage, Fighter', 'Center of the Universe', 'Mount Targon', '1');

insert into Champion
values('Anivia', '1', '3150', 'Mage, Controller', 'Rebirth', 'Freljord, Avarosan', '1');

insert into Champion
values('Aatrox', '1', '6300', 'Fighter, Tank', 'Blood Well', 'Independent', '1');

insert into Champion
values('Akali', '1', '3150', 'Slayer', 'Twin Disciplines', 'Ionia, Kinkou Order', '1');

insert into Champion
values('Annie', '1', '450', 'Mage', 'Pyromania', 'The Grey Order', '1');

insert into Champion
values('Amumu', '1', '450', 'Tank, Mage', 'Cursed Touch', 'Independent', '1');

insert into Champion
values('Garen', '1', '450', 'Fighter, Tank', 'Perseverance', 'Demacia', '1');

insert into Champion
values('Ryze', '1', '450', 'Mage, Fighter', 'Arcane Mastery', 'Independent', '1');

insert into Champion
values('Poppy', '1', '450', 'Tank, Fighter', 'Iron Ambassador', 'Bandle City, Demacia', '1');

insert into Champion
values('Ashe', '1', '450', 'Marksman, Controller', 'Frost Shot', 'Freljord, Avarosan', '1');

insert into Champion
values('Kayle', '1', '450', 'Fighter, Controller', 'Holy Fervor', 'Demacia', '1');

insert into Champion
values('Nunu', '1', '450', 'Controller, Fighter', 'Visionary', 'Freljord, Avarosan', '1');

insert into Champion
values('Master Yi', '1', '450', 'Slayer, Fighter', 'Double Strike', 'Ionia', '1');

insert into Champion
values('Soraka', '1', '450', 'Controller, Mage', 'Salvation', 'Independent', '1');

insert into Champion
values('Teemo', '1', '1350', 'Marksman, Slayer', 'Camouflage', 'Bandle City', '1');

insert into Champion
values('Ahri', '1', '4800', 'Mage, Slayer', 'Essence Theft', 'Independent', '1');

insert into Champion
values('Blitzcrank', '1', '3150', 'Tank, Fighter', 'Mana Barrier', 'Zaun', '1');

insert into Champion
values('Ezreal', '1', '4800', 'Marksma, Mage', 'Rising Spell Force', 'Piltover', '1');

insert into Champion
values('Cassiopeia', '1', '4800', 'Mage', 'Serpentine Grace', 'Noxus', '1');

insert into Champion
values('Bard', '1', '6300', 'Controller, Mage', 'Travelers Call', 'Independent', '1');

insert into Champion
values('Hecarim', '1', '4800', 'Fighter, Tank', 'Warpath', 'Shadow Isles', '1');

insert into Champion
values('Lucian', '1', '6300', 'Marksman', 'Lightslinger', 'Demacia', '1');

insert into Champion
values('Riven', '1', '4800', 'Fighter, Slayer', 'Runic Blade', 'Independent', '1');

insert into Champion
values('Thresh', '1', '6300', 'Controller, Fighter', 'Damnation', 'Shadow Isles', '1');

insert into Champion
values('LeBlanc', '1', '3150', 'Slayer, Mage', 'Mirror Image', 'Noxus, The Black Rose', '1');

insert into Champion
values('Jhin', '1', '6300', 'Marksman, Slayer', 'Whisper', 'Ionia', '1');

insert into Champion
values('Jinx', '1', '6300', 'Marksman', 'Get Excited!', 'Zaun', '1');

insert into Champion
values('Renekton', '1', '4800', 'Fighter, Tank', 'Reign of Anger', 'Independent', '1');

insert into Champion
values('Rengar', '1', '6300', 'Slayer, Fighter', 'Unseen Predator', 'Independent', '1');

insert into Champion
values('Trundle', '1', '4800', 'Fighter, Tank', 'Kings Tribute', 'Freljord, Frostguard, The Frost Trolls', '1');

insert into Champion
values('Viktor', '1', '4800', 'Mage', 'Glorious Evolution', 'Zaun', '1');

insert into Champion
values('Alistar', '1', '1350', 'Tank, Controller', 'Trample', 'Independent', '1');

insert into Player_Purchase_Champion
values('Bjergsen', 'NA', 'Cassiopeia', '4800', 'IP');

insert into Player_Purchase_Champion
values('Bjergsen', 'NA', 'Rengar', '6300', 'IP');

insert into Player_Purchase_Champion
values('Bjergsen', 'NA', 'Yasuo', '6300', 'IP');

insert into Player_Purchase_Champion
values('Bjergsen', 'NA', 'Zac', '6300', 'IP');

insert into Player_Purchase_Champion
values('Aphromoo', 'NA', 'Alistar', '1350', 'IP');

insert into Player_Purchase_Champion
values('BunnyFuFuu', 'NA', 'Alistar', '1350', 'IP');

insert into Player_Purchase_Champion
values('BunnyFuFuu', 'NA', 'Zac', '6300', 'IP');

insert into Player_Purchase_Champion
values('Meateos', 'NA', 'Zac', '6300', 'IP');

insert into Player_Purchase_Champion
values('Meateos', 'NA', 'Yasuo', '6300', 'IP');

insert into Item
values('Amplifying Tome', '435', 'null', 'null', 'Basic');

insert into Item
values('B.F.Sword', '1300', 'null', 'null', 'Basic');

insert into Item
values('Blasting Wand', '850', 'null', 'null', 'Basic');

insert into Item
values('Boots of Speed', '300', 'null', 'null', 'Basic');

insert into Item
values('Brawlers Gloves', '400', 'null', 'null', 'Basic');

insert into Item
values('Cloak of Agility', '800', 'null', 'null', 'Basic');

insert into Item
values('Cloth Armor', '300', 'null', 'null', 'Basic');

insert into Item
values('Faerie Charm', '125', 'null', 'null', 'Basic');

insert into Item
values('Needlessly Large Rod', '1250', 'null', 'null', 'Basic');

insert into Item
values('Null-Magic Mantle', '450', 'null', 'null', 'Basic');

insert into Item
values('Pickaxe', '875', 'null', 'null', 'Basic');

insert into Item
values('Prototype Hex Core', '0', 'null', 'null', 'Basic');

insert into Item
values('Rejuvenation Bead', '150', 'null', 'null', 'Basic');

insert into Item
values('Sapphire Crystal', '350', 'null', 'null', 'Basic');

insert into Item
values('The Dark Seal', '350', 'null', 'null', 'Basic');

insert into Item
values('Ancient Coin', '350', 'null', 'null', 'Basic');

insert into Item
values('Relic Shield', '350', 'null', 'null', 'Basic');

insert into Item
values('Spellthiefs Edge', '350', 'null', 'null', 'Basic');

insert into Item
values('Hunters Machete', '350', 'null', 'null', 'Basic');

insert into Item
values('Hunters Talisman', '350', 'null', 'null', 'Basic');

insert into Item
values('Cull', '450', 'null', 'null', 'Basic');

insert into Item
values('Dorans Blade', '450', 'null', 'null', 'Basic');

insert into Item
values('Dorans Ring', '400', 'null', 'null', 'Basic');

insert into Item
values('Dorans Shield', '450', 'null', 'null', 'Basic');

insert into Item
values('The Black Spear', '0', 'null', 'null', 'Basic');

insert into Item
values('Aether Wisp', '850', '415', 'null', 'Advanced');

insert into Item
values('Bamis Cinder', '1100', '700', 'null', 'Advanced');

insert into Item
values('Catalyst of Aeons', '1100', '350', 'null', 'Advanced');

insert into Item
values('Caulfields Warhammer', '1100', '400', 'null', 'Advanced');

insert into Item
values('Chain Vest', '800', '500', 'null', 'Advanced');

insert into Item
values('Chalic of Harmony', '900', '200', 'null', 'Advanced');

insert into Item
values('Crystalline Bracer', '650', '100', 'null', 'Advanced');

insert into Item
values('Executioners Calling', '800', '450', 'null', 'Advanced');

insert into Item
values('Fiendish Codex', '900', '465', 'null', 'Advanced');

insert into Item
values('Forbidden Idol', '550', '300', 'null', 'Advanced');

insert into Item
values('Giant Slayer', '1000', '650', 'null', 'Advanced');

insert into Item
values('Giants Belt', '1000', '600', 'null', 'Advanced');

insert into Item
values('Glacial Shroud', '1000', '350', 'null', 'Advanced');

insert into Item
values('Haunting Guise', '1600', '765', 'null', 'Advanced');

insert into Item
values('Hexdrinker', '1300', '500', 'null', 'Advanced');

insert into Item
values('Hextech Revolver', '1200', '330', 'null', 'Advanced');

insert into Item
values('Jaurims Fist', '1200', '450', 'null', 'Advanced');

insert into Item
values('Kindlegem', '800', '400', 'null', 'Advanced');

insert into Item
values('Last Whisper', '1300', '425', 'null', 'Advanced');

insert into Item
values('Lost Chapter', '900', '115', 'null', 'Advanced');

insert into Item
values('Negatron Cloak', '720', '270', 'null', 'Advanced');

insert into Item
values('Phage', '1250', '500', 'null', 'Advanced');

insert into Item
values('Quicksilver Sash', '1300', '850', 'Quicksilver', 'Advanced');

insert into Item
values('Recurve Bow', '1000', '400', 'null', 'Advanced');

insert into Item
values('Seekers Armguard', '1200', '165', 'null', 'Advanced');

insert into Item
values('Serrated Dirk', '1100', '400', 'null', 'Advanced');

insert into Item
values('Sheen', '1050', '700', 'null', 'Advanced');

insert into Item
values('Spectres Cowl', '1200', '350', 'null', 'Advanced');

insert into Item
values('Stinger', '1100', '500', 'null', 'Advanced');

insert into Item
values('Tear of the Goddess', '750', '275', 'null', 'Advanced');

insert into Item
values('The Hex Core mk-1', '1000', '1000', 'null', 'Advanced');

insert into Item
values('Vampiric Scepter', '900', '550', 'null', 'Advanced');

insert into Item
values('Wardens Mail', '1000', '400', 'null', 'Advanced');

insert into Item
values('Frostfang', '850', '375', 'null', 'Advanced');

insert into Item
values('Nomads Medallion', '850', '225', 'null', 'Advanced');

insert into Item
values('Targons Brace', '850', '350', 'null', 'Advanced');

insert into Item
values('Zeal', '1300', '600', 'null', 'Advanced');

insert into Item
values('Dagger', '300', 'null', 'null', 'Basic');

insert into Item
values('Kircheis Shard', '750', '450', 'null', 'Advanced');

insert into Item
values('Phantom Dancer', '2550', '650', 'null', 'Legendary');

insert into Item
values('Hextech Gunblade', '3400', '850', 'Lightning Bolt', 'Mythical');

insert into Item
values('Hextech Protobelt-01', '2500', '650', 'Fire Bolt', 'Legendary');

insert into Item
values('Banner of Command', '2900', '500', 'Promote', 'Mythical');

insert into Item
values('ZzRot Portal', '2700', '780', 'Void Gate', 'Mythical');

insert into Item
values('Long Sword', '350', 'null', 'null', 'Basic');

insert into Item
values('Tiamat', '1200', '350', 'Crescent', 'Advanced');

insert into Item
values('Corrupting Potion', '500', '350', 'Consume', 'Consumable');

insert into Item
values('Refillable Potion', '150', 'null', 'Consume', 'Consumable');

insert into Item
values('Hunters Potion', '400', '250', 'Consume', 'Consumable');

insert into Item
values('Health Potion', '50', 'null', 'Consume', 'Consumable');

insert into Item
values('Vision Ward', '75', 'null', 'Ward', 'Consumable');

insert into Item
values('Ruby Crystal', '400', 'null', 'null', 'Basic');

insert into Item
values('Sightstone', '800', '400', 'Warding', 'Advanced');

insert into Item
values('Ruby Sightstone', '1600', '400', 'Warding', 'Legendary');

insert into Item
values('Eye of the Equinox', '2200', '550', 'Warding', 'Legendary');

insert into Item
values('Talisman of Ascension', '2500', '450', 'Movement Speed', 'Mythical');

insert into Item
values('Randuins Omen', '2900', '900', 'Slow', 'Legendary');

insert into Item
values('Elixir of Iron', '500', 'null', 'Consume', 'Consumable');

insert into Item
values('Elixir of Sorcery', '500', 'null', 'Consume', 'Consumable');

insert into Purchase_And_Upgrade
values('SneakyGASM', 'NA', 'Sightstone', '800', '400', 'IP');

insert into Purchase_And_Upgrade
values('DoubleLift', 'NA', 'Phantom Dancer', '2550', '650', 'IP');

insert into Purchase_And_Upgrade
values('Bjergsen', 'NA', 'Elixir of Sorcery', '500', 'null', 'IP');

insert into Purchase_And_Upgrade
values('mid fekar', 'NA', 'Randuins Omen', '2900', '900', 'IP');

insert into Purchase_And_Upgrade
values('BunnyFuFuu', 'NA', 'Talisman of Ascension', '2500', '450', 'IP');

insert into Item_upgradeInto_Item
values('Ruby Crystal', 'Sightstone', '400');

insert into Item_upgradeInto_Item
values('Refillable Potion', 'Corrupting Potion', '350');

insert into Item_upgradeInto_Item
values('Randuins Omen', 'Ruby Crystal', '2500');

insert into Item_upgradeInto_Item
values('Sightstone', 'Ruby Sightstone', '800');

insert into Item_upgradeInto_Item
values('Sightstone', 'Eye of the Equinox', '1400');

insert into Champion_Wield_Item
values('Jhin', 'Sightstone');

insert into Champion_Wield_Item
values('Alistar', 'Talisman of Ascension');

insert into Champion_Wield_Item
values('Thresh', 'Randuins Omen');

insert into Champion_Wield_Item
values('Annie', 'Hextech Protobelt-01');

insert into Champion_Wield_Item
values('Riven', 'Tiamat');

insert into Champion_Skills1
values('Jhin', 'Active',
'Launches a canister at the target enemy, dealing physical damage. Dancing Grenade bounces to up to three additional enemies beyond the first, with each kill it performs increasing its total damage by 35%.',
'1', 'MIN. PHYSICAL DAMAGE: 50 / 75 / 100 / 125 / 150 (+ 30 / 35 / 40 / 45 / 50% AD) (+ 60% AP)');

insert into Champion_Skills1
values('Alistar', 'Passive',
'Alistar removes all hostile crowd control effects from himself. Additionally, he gains bonus attack damage and takes reduced damage for 7 seconds.',
'1', 'BONUS ATTACK DAMAGE: 60 / 75 / 90 AD. DAMAGE REDUCTION: 50 / 60 / 70%');

insert into Champion_Skills1
values('Alistar', 'Active',
'Alistar dashes to the target enemy, dealing magic damage, stunning them for 1 second and knocking them back.',
'3', 'MAGIC DAMAGE: 55 / 110 / 165 / 220 / 275 (+ 70% AP)');

insert into Champion_Skills1
values('Ahri', 'Active', 'Ahri blows a kiss in a line, dealing magic damage to the first enemy hit and charming them.',
'5', 'MAGIC DAMAGE: 60 / 95 / 130 / 165 / 200 (+ 50% AP). CHARM DURATION: 1 / 1.25 / 1.5 / 1.75 / 2');

insert into Champion_Skills1
values('Kayle', 'Active', 'Kayle bathes the target allied champion or herself in holy light, rendering them immune to all damage for a few seconds.',
'3', 'DURATION: 2 / 2.5 / 3');

insert into Champion_Skills2
values('Brand', '1', '50', '1', '7');

insert into Champion_Skills2
values('Braum', '1', '60', '7', '120');

insert into Champion_Skills2
values('Fiddlesticks', '3', '165', '1', '12');

insert into Champion_Skills2
values('Fiora', '5', '200', '2', '12');

insert into Champion_Skills2
values('Kindred', '3', '1', '3', '80');

insert into Champion_Stats1
values('Jhin', 'Attack Damage', '53', '1', '4');

insert into Champion_Stats1
values('Jhin', 'Health', '540', '1', '85');

insert into Champion_Stats1
values('Jhin', 'Mana', '300', '1', '50');

insert into Champion_Stats1
values('Jhin', 'Magic Resist', '30', '1', '0');

insert into Champion_Stats1
values('Jhin', 'Move Speed', '330', '1', '0');

insert into Champion_Stats2
values('Brand', '1', '4', '53');

insert into Champion_Stats2
values('Braum', '2', '85', '625');

insert into Champion_Stats2
values('Fiddlesticks', '3', '50', '450');

insert into Champion_Stats2
values('Kindred', '4', '0', '30');

insert into Champion_Stats2
values('Fiora', '5', '0', '330');

insert into Report_A_Player
values('001', 'Hide on Bush', 'KR', 'Meateos', 'NA' ,'183707112016',
'This guy definitely scripting');

insert into Report_A_Player
values('002', 'mid fekar', 'NA', 'SneakyGASM', 'NA' ,'234011032016',
'Intentionally troll plays xinzhao mid');

insert into Report_A_Player
values('003', 'Aphromoo', 'NA', 'DoubleLift', 'NA', '000131102015',
'He kicked me out of clg! Scrub teammate scrub clg');

insert into Report_A_Player
values('004', 'Froggen', 'NA', 'Bjergsen', 'NA', '041004112016',
'boosted monkey man top laner, no counterplay');

insert into Report_A_Player
values('005', 'Jensen', 'NA', 'Impact', 'NA', '193015102016',
'Top die but my team mid die');

insert into Report_A_Player
values('006', 'DoubleLift', 'NA', 'LemonNation', 'NA', '133026052016',
'Flamed me for roaming');

insert into Report_A_Player
values('007', 'DoubleLift', 'NA', 'Hai', 'NA', '183026052016',
'Took my blue buff');


insert into Report_A_Player
values('009', 'DoubleLift', 'NA', 'SneakyGASM', 'NA', '093031052016',
'Didnt meme enough in game');

insert into Report_A_Player
values('010', 'DoubleLift', 'NA', 'Meateos', 'NA', '093005052016',
'Didnt reach quarterfinals at worlds');

insert into Report_A_Player
values('011', 'DoubleLift', 'NA', 'Kikis', 'EU', '094005052016',
'Got a penta kill against me..');

insert into Report_A_Player
values('012', 'DoubleLift', 'NA', 'FNC Febiven', 'EU', '105005052016',
'Told me to uninstall the game');

insert into Report_A_Player
values('013', 'DoubleLift', 'NA', 'FNC Rekkles', 'EU', '105105052016',
'Told Febiven to uninstall the game');

insert into Report_A_Player
values('014', 'DoubleLift', 'NA', 'G2 ZV3N', 'EU', '105205052016',
'Told Febiven to uninstall the game');

insert into Report_A_Player
values('015', 'DoubleLift', 'NA', 'Ssumday', 'KR', '105305052016',
'Told Febiven to uninstall the game');

insert into Report_A_Player
values('016', 'DoubleLift', 'NA', 'Crown', 'KR', '105405052016',
'Told Febiven to uninstall the game');

insert into Report_A_Player
values('017', 'DoubleLift', 'NA', 'Smeb', 'KR', '105505052016',
'Told Febiven to uninstall the game');

insert into Report_A_Player
values('018', 'DoubleLift', 'NA', 'PraY', 'KR', '105605052016',
'Told Febiven to uninstall the game');

insert into Report_A_Player
values('019', 'DoubleLift', 'NA', 'Bang', 'KR', '105705052016',
'Told Febiven to uninstall the game');

insert into Report_A_Player
values('020', 'DoubleLift', 'NA', 'Rush', 'KR', '105805052016',
'Told Febiven to uninstall the game');

insert into Report_A_Player
values('021', 'DoubleLift', 'NA', 'Jensen', 'NA', '105905052016',
'Told Febiven to uninstall the game');

insert into Report_A_Player
values('022', 'DoubleLift', 'NA', 'Impact', 'NA', '110005052016',
'Told Febiven to uninstall the game');

insert into Report_A_Player
values('023', 'DoubleLift', 'NA', 'Huni', 'NA', '110105052016',
'Told Febiven to uninstall the game');

insert into Report_A_Player
values('024', 'DoubleLift', 'NA', 'Hauntzer', 'NA', '110205052016',
'Thought Gengar was better');

insert into Report_A_Player
values('025', 'DoubleLift', 'NA', 'Froggen', 'NA', '110305052016',
'Told Febiven to uninstall the game');

insert into Report_A_Player
values('026', 'DoubleLift', 'NA', 'BunnyFuFuu', 'NA', '110405052016',
'Told Febiven to uninstall the game');

insert into Report_A_Player
values('027', 'DoubleLift', 'NA', 'Aphromoo', 'NA', '110505052016',
'Told Febiven to uninstall the game');

insert into Report_A_Player
values('028', 'DoubleLift', 'NA', 'mid fekar', 'NA', '093131052016',
'Didnt meme enough in game');

insert into Report_A_Player
values('029', 'DoubleLift', 'NA', 'Hide on Bush', 'KR', '093231052016',
'Didnt meme enough in game');

insert into Report_A_Player
values('030', 'DoubleLift', 'NA', 'Bjergsen', 'NA', '093331052016',
'Didnt meme enough in game');

insert into Report_A_Player
values('031', 'DoubleLift', 'NA', 'LemonNation', 'NA', '093431052016',
'Didnt meme enough in game');

insert into Report_A_Player
values('032', 'DoubleLift', 'NA', 'EDG DEFT', 'KR', '095005052016',
'Took ADC from me and feeds..');




