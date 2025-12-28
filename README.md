# Admin

This application is taking care of user management and deck building.


See [Matag: The Game](https://github.com/MatagTheGame/matag-the-game/wiki) wiki


## Automated Build

![Java CI with Maven](https://github.com/MatagTheGame/matag-admin/workflows/Java%20CI%20with%20Maven/badge.svg)
 - https://github.com/MatagTheGame/matag-admin/actions 
 

## Requisites

Read [Requisites](https://github.com/MatagTheGame/game/wiki/Requisites)
 
The application connects to a [postgresql](https://www.postgresql.org/) database.
You can install one locally and then run the [postgres-schema.sql](src/main/resource/schema.sql)

## Development

The application is written using:
 * Java ([Spring](https://spring.io/))
 * Javascript ([React](https://reactjs.org/) + [Redux](https://redux.js.org/))

The use of an IDE like [IntelliJ](https://www.jetbrains.com/idea/download/) will help much during development.
(Community edition is available).


### Build

Build java:

    mvn install
    
Build js:

    yarn install
    yarn watch
    
(To have more helps with imports click on the js folder and mark it as resource root)
    

## Tests

### Run Tests locally

You can run all tests with:

    mvn test

Tests mock database interaction so you should be able to code without it.


### Run the application locally

Startup the app as spring boot

    # from intellij or with
    mvn spring-boot:run -DDB_URL=<db_url> -DDB_NAME=<db_name> -DDB_USERNAME=<db_username> -DDB_PASSWORD=<db_password> -Dserver.port=8082 -Dmatag.admin.url=http://localhost:8082 -Dmatag.game.url=http://localhost:8080 -Dmatag.admin.password=password
    
(The values above depend on your configuration.)


### Useful queries
    
    -- select
    select * from matag_user;
    select * from matag_user_verification;
    select * from matag_session;
    select * from game;
    select * from game_session;
    select * from score;
    
    -- composite
    select * from game g join game_session gs on g.id = gs.game_id;
    
    
    -- delete stuff
    delete from game_session where game_id in (select game_id from game where status != 'FINISHED');
    delete from game where status != 'FINISHED';
    delete from matag_session;
    delete from matag_session where valid_until < current_timestamp;
    delete from matag_user where id in (24, 25);
    
    
    -- drop everything
    drop table game_session;
    drop table game;
    drop table matag_session;
    drop table matag_user_verification;
    drop table matag_user;
    drop table score;
    
    -- change stuff
    ALTER TABLE matag_user ALTER COLUMN type SET NOT NULL;
    ALTER TABLE game add column finished_at timestamp;
    ALTER TABLE matag_user drop column status;
