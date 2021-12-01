use catchmind;

show tables;

CREATE TABLE userTbl(
	mNum INT PRIMARY KEY auto_increment,
	mID VARCHAR(20) NOT NULL UNIQUE,
	mPw VARCHAR(30) NOT NULL
);

INSERT INTO userTbl(mID,mPw) VALUES('root', 'root');

SELECT * FROM userTbl;
