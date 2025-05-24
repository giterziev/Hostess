# :woman_pilot: Hostess
Hostess aims to be a general-purpose DNS blocker and hosts file updater, reliant on publicly available compilation of hosts lists populated by malware, adware, tracking, and all-around unwanted addresses that, with the help of the computer’s hosts file, can be redirected in a way that they don’t leave the device, assuring privacy and security.
Inspired by the popular application for Android called AdAway, which unfortunately doesn’t have a version for other Operating Systems, we decided to choose this project because it offers an easy and passive
way of protection for users who aren’t too technologically savvy and/or don’t want to be manually reviewing the connections from their favorite programs.

> [!IMPORTANT]
> The GUI is horrible. No way to sugarcoat it. Not my forte. You've been warned.
> If anyone wants to fork and improve on it... thank you!

# :running_woman: How to run
1. It needs JRE 21 that can be obtained from [here](https://adoptium.net/es/temurin/releases/?version=21&arch=any&os=windows&package=jre).
2. After installing the JRE, just run  Hostess.jar.
3. Wait for an alert stating "Loaded initial configurations!"
4. Start adding/removing URLs using the side tabs (They're independant and you're not required to use them all).
5. Press "Update" on the top right of the application.

# :stopwatch: Quick start
## Sources Tab
>**Add or remove URLs that lead to online hosts files and will be downloaded and merged with your local hosts file.**

>EX: [StevenBlack](https://raw.githubusercontent.com/StevenBlack/hosts/master/hosts)

## Local Tab
>**A textbox to add custom URLs that will merged with your local hosts file.**

>EX: You can add your current hosts file entries, in case you have custom ones, and/or add URLs that you've found your PC connects to.

## Exclusions Tab
>**Add or remove URLs that you want to EXCLUDE (DELETE) from the downloaded hosts files BEFORE being merged with your local hosts file.**

>EX: Useful if one of the lists you're using blocks a certain page that you don't want blocked.

# :interrobang: Error Messages
## This entry already exists in the list!
>It pops up when you are adding an entry that's already in Source/Exclusion. 

## The URL appears to be invalid! Please check and try again!
>It pops up when the entry added to Source is invalid.

> [!NOTE]
> In this alfa version of hostess, *timeout* errors aren't being caught so the application will freeze for a few seconds until it shows the error.
> 
> I advise you check the URLs **BEFORE** adding them to hostess to avoid this inconvenience.

## Hostess will now merge to your hosts file! Close this dialogue and wait patiently!
>Pops up after pressing "Update" and, upon closing it, hostess will start merging.

> [!NOTE]
> Depending on the amount of entries in Source, this might take more or less time.
>
> Please be patient and wait for a confirmation alert.

## Successfully written to hosts file!
>Shows when the merging is complete.

> [!NOTE]
> A known bug is that if you cancel the elevetaion of permissions, hostess will still proceed and this message will be shown however, no entries will be added since the hosts file needs the elevetaion.

# :question: FAQ
>**Q. Why Hostess**

>*A. Because it starts with "host" and thought it might be an amusing play on words.*

>**Q. Privacy**

>*A. Hostess only connects to the internet when downloading the files from the sources you've added manually.
>You should, however, check with the privacy clauses of the pages hosting those lists.*

>**Q. Should I report issues?**

>*A. Please don't. Please.*

>**Q. Can I fix this... things?**

>*A. Yes, please. Fork/fix/burn to the ground. Your choice!*
