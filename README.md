# NaaS: Navigation as a Service

### Index



#### 1  Motivation

#### 2  Problem Statement

#### 3  High Level Architecture

#### 4  Flow Diagram

#### 5  Literature Review

#### 6  Smartphone Solution

#### 7  Tools and Platforms Used

#### 8  Functional Flow

#### 9  Cloud Connectivity

#### 10  Implementation

#### 11  Physical System Architecture

#### 12  Challenges

#### 13  Human Interaction

#### 14  Conclusion

#### 15  References


### Motivation

#### Our motivation for this project stems from the fact that people are increasingly relying

#### upon their smartphones to solve some of their common daily problems. One such

#### problem that smartphones have not yet completely solved is ​ Indoor Navigation ​. At the

#### time of writing, there is not a single low-cost scalable mobile phone solution available in

#### the market that successfully navigated a user from one position to another indoors.

#### An indoor navigation app would certainly benefit users who are unfamiliar with a place.

#### Tourists, for instance, would have a better experience if they could navigate confidently

#### inside a tourist attraction without any assistance. In places such as museums and art

#### galleries, the application could be extended to plan for the most optimal or ‘popular’

#### routes. Such a system could also be integrated at airports to navigate passengers to

#### their boarding gates. Similarly an indoor navigation system could also benefit local

#### users

#### who have previously visited the location but are still unaware of the whereabouts of

#### some of the desired items. These include supermarkets, libraries

#### and shopping malls. The application could also benefit clients who install the

#### system by learning user behaviours and targeting advertisements at specific

#### locations.

#### Compare and buy ​ : Indoor navigation app will also let the local users to compare the

#### prices of shopping items in different stores in the same vicinity. This feature could save

#### their precious time from roaming around and searching for the right product at the right

#### place and at the right price.

#### Footfalls Analytics ​: Businesses find extremely tough to understand the customer

#### behaviour and expressing themselves according to their needs. An indoor app

#### integrated with footfalls analysis would let the businesses unserstand where the

#### customer is spending most of his time. Also, our location-based-notification service like

#### premiums and coupons would help in enhancing customer experience and boost sales.

#### Emergency PathFinder ​ : Emergency situations like earthquakes and fire are likely to

#### arise and can come at anytime. Performing emergency evacuation task during these

#### emergencies becomes very hectic as situation worsens so our "Emergency" feature

#### would speed up the evacuation by providing the path to the nearest exit to the users.


### Problem Statement
When we consider a massive public structure like airports and railway stations and also
commercial centres like shopping malls, people face a few problems that aren't addressable by
traditional methods. A few of them are
a) Finding the parking space , fastest route to check-in counters , terminals or gates ,
ATM’s , restrooms , Belt-drives , exit and public transports like cab and bus at airports to finding
the right platforms , ticket and enquiry counter , waiting rooms, food court etc at these
structures is troublesome and indeed consumes a lot of time.
b)Footfall analysis and measurement of Influx/Outflux of customers is also difficult. The
measurement of visitor flows can benefit the management in analysing “heatmaps” where
most customers visit and the retail centres can be rented according to the customers.
c) Staff management can also be a challenge in these huge structures. Eg.
Management of Cleaning staff. Personnel / Staff tracking is required to relocate services
where needed the most. Hence staffs and personnel should be monitored remotely along
with the instructions to serve the customers better.
With all these requirements satisfied, a revenue model must also be framed for maintenance
of the system which solves the above mentioned problems.

### A High Level Architecture and Flow Diagram

Our idea is mainly meant for public places like airports and railways and also for commercial
places like shopping malls. They have a vast and complex infrastructure and it’s always a difficult
task to keep track of all things within it. We can deploy indoor navigation system which has the
following functionalities.

1. **Location based services (LBS)** ​​: For Convenience of the users,Indoor position
    determination is an important consideration for retail – indoor navigation helps clients find
    their way in large supermarkets and malls via app. Also , it can help in increasing sales by
    providing premium ads and coupons through push-notifications. But the technology is equally
    suitable for public buildings with shopping areas, such as airports and railway stations.
    Depending on the application case, positioning is realized with the help of beacons, Wi-Fi or
    VLC.


3. **Footfall analysis and Visitor Flows:** ​​or operators of shopping centers, airports, trade fairs,
    railway stations etc. it is interesting to know how many visitors stay at a certain place at a
    certain time and how they move. This way, it is possible to display on a heat map, which
    places are most frequented – which can be useful for the security staff and others. An alarm
    can be given when a predefined amount of people stays at a point in the building. After that,
    actions can be triggered, for example an unscheduled cleaning or a redirection of itineraries.
    For the marketing department it is interesting to know such numbers. They can also be used
    to direct people to certain shops or advertising spaces. Organizers of trade fairs profit from
    the technology as they get some precise visitor numbers when negotiating with exhibitors.
       _Figure: A Very High Level View of the flow_


### Literature Review

#### 1. Beacons

#### a. Indoor Navigation

#### infSoft, a German based company has been offering solutions in the area

#### of indoor navigation, indoor analytics, indoor tracking and location-based

#### services since 2005. Besides the development of all-in-one solutions for

#### major clients, infsoft offers scalable Software Development Kits (SDK),

#### which lets developers integrate our key technologies into third party

#### applications. Frankfurt Airport, Swiss Federal Railways (SBB), Siemens

#### and Roche are among our long-standing clients.

#### inSoft also provides a free WhitePaper for an introduction to the topic of

#### Indoor Navigation which can be downloaded from their website

#### https://www.infsoft.com/

#### b. Proximity Marketing

#### BeaconStac is another company which provides solution for proximity

#### marketing. Google, Penn National Guard, KFC and Uber are a few of its

#### clients. They provide a free and a comprehensive guide on proximity

#### marketing in their website which is an extensively well written.

#### https://www.beaconstac.com/

#### However, it is to be noted that, infsoft and beaconstac are commercial vendors of

#### Beacons and SDKs and their products are proprietary. Hence we use the literature

#### concepts from these sources and implement the project using the Open-Source Android

#### Beacon Library.

#### Research Paper:

#### “A Measurement Study of BLE iBeacon and Geometric Adjustment Scheme for

#### Indoor Location-Based Mobile Applications ​”

#### By Jeongyeup Paek,1 JeongGil Ko,2 and Hyungsik Shin

#### 1 School of Computer Science and Engineering, Chung-Ang University, Seoul, Republic

#### of Korea

#### 2 Department of Software and Computer Engineering, Ajou University, Suwon, Republic

#### of Korea

#### 3 School of Electronic and Electrical Engineering, Hongik University, Seoul, Republic of

#### Korea

#### Link: http://downloads.hindawi.com/journals/misy/2016/8367638.pdf


### Libraries

#### Android Beacon Library

#### It allows Android devices to use beacons much like iOS devices do. An app can request

#### to get notifications when one or more beacons appear or disappear. An app can also

#### request to get a ranging update from one or more beacons at a frequency of

#### approximately 1Hz. It also allows Android devices to send beacon transmissions, even

#### in the background.

#### Library Link: ​https://altbeacon.github.io/android-beacon-library/

#### Documentation Link:

#### https://altbeacon.github.io/android-beacon-library/documentation.html

#### Library: ​https://github.com/AltBeacon/android-beacon-library

#### The creator of android beacon library, David G Young

#### (​http://www.davidgyoungtech.com/​) has created a demo application on a few of the

#### features of the android beacon library through GitHub and video references on

#### YouTube. Continuous support is provided for free through StackOverFlow:

#### https://stackoverflow.com/users/1461050/davidgyoung

#### And issues also can be raised through GitHub:

#### https://github.com/AltBeacon/android-beacon-library/issues

#### The documentation, Library, demonstration and the feature of raising issues were

#### invaluable sources of information for planning the architecture of the application,

#### implementing it and also rectifying and debugging the issues found in the project.

#### Apart from these references, certain books were used as references to implement the

#### web application and mobile application. They are cited in the references.


### Tools and Platforms that will be used

#### Software

```
1.Altbeacon Library
2.Android Studio
3.Flask Microframework
4.Google Firebase
5.AWS EC
6.Databases: MySQL and MongoDB
```
#### Hardware

```
1.Bluetooth Beacons
2.Android Smartphone
```

### Functional Flow

```
Fig: Flowchart
```

The Functional Flow for the three scenarios are given below:

1. Indoor Navigation
    a. The Beacon signals will be caught by the smartphone and trilateration will
       fetch the real time location of the user.
    b. The real time location is displayed on the smartphone screen and relevant
       guidance is given to the user if he/she wants to navigate to a particular location.
    c. The location is tagged with the nearby “places of interest” i.e. coffee shops,
       restaurants etc. and with the bluetooth ID and is sent to the server.
<Location, Bluetooth ID, {Places of Interest}> is the payload
This feature will be helpful for the customers to navigate in instances such as
a. Finding the platform to aboard train/airplane
b. PalFinder: PalFinder will be a feature where a person can meet another
person by navigating to his current position. Talking over the phone or texting is a non intuitive
way of finding any friend in such huge structure especially when the structure is not familiar.
Curious Kids can also be tracked by the parents and hence will be ensured that their kids will
be safe and won’t get lost in pursuit of curiosity!
2. Premiums and Coupons
a. Based on the nearby “Places of interest”, coupons and premiums will be
delivered to the customer’s smartphone so that the customer can know about
the best offers and can make an immediate selection for a place for
shopping/recreation etc
.
2) A huge crowd without any trains inbound or outbound may indicate a condition like medical
emergency or any accident or any other incident.


### Cloud Connectivity with Database Interaction

```
Figure: Cloud Connectivity with Database Interaction
```

### Implementation

**Web application**
This web application is for store owners.They can use this web application for adding
items, updating items ,deleting items ,adding and updating coupons and advertisements
and to check footfall data.
**Front end**
-HTML
-CSS
-Javascript
-Bootstrap
**Backend**
-Flask
**Database**
-mongodb(mLab)
The frontend tools were used to build the portal for Store owners to add the coupons and
product data which used to reside in the database. REST API was built for the android
application to query the database whenever required. Navigation Data is also added through
Database itself. Database is hosted in mLab and website on Heroku.
**Android Application**
With the help of android application, the users can
Navigate to the desired location
Navigate to nearest location during emergency
Compare and Buy Products from various stores
Look out for Coupons and Premiums through Proximity Marketing
**The Major Libraries and technologies used are**
Android Beacon Library
Volley for REST API
Firebase RealTime Database
Handler Thread
Android application would load the map based on the proximity beacons. As the beacons
change, position of the person would also change and this is achieved by a thread whose
function is to query the database and load the map based on the location identified by the
bluetooth beacons. Parameters required for the API call are Source and Destination locations.
Compare and Buy feature would query the database and listen for changes. The Parameters for
the API are Product Name and Category. Coupons would be loaded based on the nearest
shops. The beacons would be identified and appropriate Coupons would be loaded based on
proximity marketing.


### Data Analytics to be deployed

Data Analytics algorithms are to be deployed in our application. Since we aren’t collecting any
personal data from the user, we wouldn’t be able to deploy machine learning algorithms since
they aren’t feasible. The Data analytics programs make use of the Numpy and Pandas
Library and are helpful in

1. Footfall analytics: The count of the people, gender and age of the people that enter
    a particular restaurant or a store can be worked upon to know the customers well.
2. Heatmap Generation: The density of people in a particular area for timely dispatch
    of staff members. Using functions, we can show the heatmap for the user.
3. Prediction of the amount of people: Again, as the previous point, it will help
    the management in managing the staffs.


### Physical system architecture (Beacons)

The beacons must be placed appropriately such that the signals are caught by the phone in a
very dependable manner. This requires planning the number of bluetoooth beacons and their
locations depending upon the architecture of the building and number of floors.
The following challenges have to be taken care of
**Challenges :**
1) If user stands exactly in between two stores, he might get multiple notification at
the same time which can be overwhelming.
2) Since this is a centralized architecture, failure of server may result in loss of
service across all beacons.
_Figure: Human Interaction_


### Human Interaction

Users will approach a store and signals emitted by the beacon installed in the store, will be
captured by the user’s smartphone. The location of the user along with the tags and Bluetooth
ID is sent to the server by the Android app. The server then stores all the information which will
pave way for footfall analysis. The coupons and premiums relevant to the store near which the
users are located are displayed to the user on their smartphone through notifications.
**Algorithm**

1. The User Downloads and Installs the app. Each user is identified by the smartphone’s
    bluetooth address.
2. Some basic questions will be answered by the user to determine the user’s age, gender
    and other attributes. No personal information is sought after and privacy is protected.
3. The user than can get the current coordinates in tha layout map of the structure a.k.a.
    Airport, railway station etc.
4. Navigation can be done by entering a location’s name. On entering a valid location,
    one or more ‘route maps’ will be obtained. The user can choose any one of the route.
5. The recommended routes will have the shortest distance and/or the routes where the
    “traffic” will be less. Hence effective traffic management of the people is done.
6. When the user decides for some recreation, coupons and premiums will be delivered
    to the smartphone based on the attributes of the person. Note that attribute will be
    asked only once per user.
7. The person can also locate a friend or a family member and navigate to his/her
    position without the hustle of asking random people or looking up sign boards.
Using analytics we will analyze the previous data. For example, On Monday morning most people
brought orange juice with potato chips from a particular store. So using this data we can tell store
owners to give combo offers and sell products which will give them more profit. And then this type of
offers can be given to customers as notifications when they are near that store.


### Conclusion

#### The Benefits of the product to the users are as follows:

#### 1. Indoor wayfinding becomes even easier with an indoor positioning system as

#### users don’t have to type in their current position when navigating from point A to

#### point B.

#### 2. Likewise, they don’t have to worry about counting doors, turns, or the like as

#### they can see their real time position on the map (blue dot) as they move through

#### the building.

#### 3. Location Based Marketing:

#### The combination of indoor navigation and indoor positioning also gives you the benefits

#### of location-based marketing. Not only does this allow you to create a more personalized

#### experience (think of sending special offers when shoppers are stopping at the pasta

#### aisle or use the information you have on stadium visitors, for instance from ticket sales,

#### to send a personalized welcoming messages),

#### So, finally one can come to a conclusion of:

#### With coin-cell battery, BLE beacons can work up to years depending on the

#### broadcasting frequency. On the other hand, GPS is traditionally thought to be power

#### hungry. The power consumption of GPS is comparable with WiFi.

#### With respect to the location accuracy, GPS is born to implement the location service,

#### whereas BLE is definitely not. BLE is mostly used to implement context-aware

#### applications. The tricky part happens at indoor environments where GPS fails. In such

#### situations, BLE may be used together with triangulation algorithms to locate objects.

#### High accuracy via proximity detection (message triggering based on user’s location in

#### relation to a beacon)

#### Links:

#### Web and Database: ​https://github.com/RohitRH/indoor-navigation

#### Android: ​https://github.com/prashanthw360/IndoorNav

#### Presentation:

#### https://drive.google.com/open?id=16XqYUXNWto8Mn67H1vplUTLrW0uPN5gc


### References

#### 1) Android Documentation, Google: ​https://developer.android.com/docs

#### 2) “Head First Android Development: A Brain-Friendly Guide”, by Dawn and David

#### Griffiths, O’Reilly Publications.

#### 3) Android Beacon Library (Guide and Documentation):

#### https://github.com/AltBeacon/android-beacon-library

#### 4) Java Documentation, Oracle: ​https://docs.oracle.com/en/java/

#### 5) Python Documentation: ​https://docs.python.org/3/

#### 6) Flask Documentation: ​https://flask.palletsprojects.com/en/1.0.x/

#### 7) MongoDB Documentation: ​https://docs.mongodb.com/

#### 8) MongoDB Manual: ​https://docs.mongodb.com/manual/

#### 9) mLab Documentation: ​https://docs.mlab.com/

#### 10) “Flask Web Development: Developing Web Applications with Python”, by Miguel

#### Grinberg



