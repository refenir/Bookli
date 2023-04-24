# Bookli

The backend will be running on localhost, and thus the ip address to fetch data from localhost will have to be changed.

To find ip address of your localhost, run ifconfig (macOS) or ipconfig /all (windows).

After finding the ip address, change the variables QUERY_FOR_BOOKINGS and QUERY_FOR_STUDENTS in the file BookingDataService.java to be the ip address.
