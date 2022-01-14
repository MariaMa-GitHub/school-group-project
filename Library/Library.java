/**
 * Library class: 
 * The library can buy new books for its collection, and people (Person class) are able to check books out, 
 * or if they're unavailable, put them on hold.
 * 
 * Bashir Kadri
 * Jan 11, 2022
 */

import java.util.Scanner;
import java.util.ArrayList;

public class Library 
{
    
    
    
    ArrayList<Person> people;
    ArrayList<Media> collection;
    Person currentUser; //**new variable
    String name;
    String address;
    static int totalUsers = 0;
    
    //open Scanner
    Scanner input = new Scanner(System.in);
    
    public Library()
    {
        //add FileIO later
        this.people = new ArrayList<Person>();
        this.collection = new ArrayList<Media>();
        this.currentUser = null;
        this.name = "TBD";
        this.address = "TBD";
    }
    public Library(String name, String address)
    {
        this.people = new ArrayList<Person>();
        this.collection = new ArrayList<Media>();
        this.currentUser = null;
        this.name = name;
        this.address = address;
    }
    
    public void printCollection()
    {
        //just print all the titles of all the items
        for (Media m : this.collection) {
            System.out.println(m.title);
        }
    }
    public void checkOut(Media m, Person p)
    {
        //**we can discuss this later, but this is how I think the program will work, tell me if we want to do it differently
        String response = "";
        
        //Check if the current Media is available or not
        if (m.availability) //is available
        {
            
            // video game age rating error checking
            if(m.id == 3){
                    if(p.age < ((VideoGames)m).rating){
                        System.out.println("\nThis rating is not compatible for your age!");
                    }
            }
            //If available, check if the Person has enough room to borrow the book
            else if (p.possessions.size() < p.MAX_POSSESSIONS)
            {
                
                System.out.println("\nInformation about the item:\n\n" + m); 
                
                // if video game, check age rating
                
                //ask user if they want to check it out
                do
                {
                    System.out.printf("\n%s is available, would you like to borrow this item? (y/n) ", m.title);
                    response = input.nextLine().toLowerCase();
                }
                while (response.equals("y") && response.equals("n"));
                
                //and then finally, if they answer yes, add to their checked out items and change the Media's availability
                if (response.equals("y"))
                {
                    System.out.printf("\nYou have succesfully checked out %s.\n", m.title);
                    p.possessions.add(m);
                    m.availability = false;
                }
                else if (response.equals("n")) //nothing changes, give them some message
                {
                    System.out.printf("\n%s has not been checked out.\n", m.title);
                }
                
            }
            else //item is available, but their "cart" is full, give some message
            {
                System.out.println("\nSorry, you have too many items currently checked out. Please try again another time.");
            }
        }
        else if (currentUser.findPossession(m)) //if the user already has current item
        {
            System.out.printf("\nSorry, you already have %s checked out. You cannot currently put this on hold.\n", m.title);
        }
        else if (currentUser.findRequest(m)) //if the user already requested for the current item
        {
            System.out.printf("\nSorry, you are already waiting in line for %s. You cannot currently put this on hold.\n", m.title);
        }
        else
        {
            do
            {
                System.out.printf("\nSorry, %s is not available, would you like to put this item on hold? (y/n) ", m.title);
                response = input.nextLine().toLowerCase();
            }
            while (!response.equals("y") && !response.equals("n"));
            
            //and then finally, if they answer yes, add them to queue, if no, do nothing
            if (response.equals("y"))
            {
                System.out.printf("You have succesfully been added to the queue for %s.\n", m.title);
                m.hold.enQueue(p);
                currentUser.requests.add(m);
            }
            else if (response.equals("n")) //nothing changes, give them some message
            {
                System.out.printf("%s has not been added to the queue.\n", m.title);
            }
        }
        
    }
    public void returnBook(Media m, Person p)
    {
        //this should be simpler, to return a book just change availability, 
        //"notify" next person in queue if there is anyone (just do this in console for now) 
        //and change Person's "cart"
        
        //first change availability
        m.availability = true;
        
        //then notify next person in queue
        if (!(m.hold.isEmpty())) //ie. is NOT empty
        {
            Person nextPerson = m.hold.deQueue();
            System.out.printf("\n%s is next to pick up the book!\n", nextPerson.name); //doing it this way removes them from the queue & returns name at the same time
            nextPerson.possessions.add(m);
            nextPerson.requests.remove(m);
            m.availability = false;
        }
        
        System.out.println("\nThe item " + m.title + " has successfully been returned.");
        
        //and finally, edit Person's cart
        p.possessions.remove(m);
        
    }
    
    //helper methods
    private Person findPerson(int target)
    {
        for (int i = 0; i < this.people.size(); i++)
        {
            if (this.people.get(i).cardNum == target)
            {
                return this.people.get(i);
            }
        }
        return null; //no match found
    }
    private Media findMedia(String target)
    {
        for (int i = 0; i < this.collection.size(); i++)
        {
            if (this.collection.get(i).title.equals(target))
            {
                return this.collection.get(i);
            }
        }
        
        return null; //no match found
    }
    public boolean printFilteredCollection(String chosenGenre){
        int counter = 0;
        for (Media m : this.collection)
        {
            if ( m.genre.equals(chosenGenre)){
                counter++;
                System.out.println(m.title);
            }
        }
        if(counter == 0){
            System.out.println("There is no media that meet your requirements.");
            return false;
        }
        else{
            return true;
        }
    }
    public static String sortByMedia(ArrayList<Media> m){
        String s1 = "";
        String s2 = "";
        String s3 = "";
        for(int i = 0; i< m.size(); i++){
            if(m.get(i).id == 1){
             s1 += m.get(i).printTitle() + "\n";
             }
             else if(m.get(i).id == 2){
                 s2 += m.get(i).printTitle() + "\n";
             }
             else{
                 s3 += m.get(i).printTitle() + "\n";
             }

         }

         String ans = "\nAUDIOBOOKS:\n" + s1 + "\nNOVELS:\n" + s2 + "\nVIDEO GAMES:\n" + s3;
         return ans;
    }
    
    private static Person createPerson() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\nCreate a library card:");
        System.out.print("What is your name? ");
        String name = sc.nextLine();
        System.out.print("How old are you? ");
        short age = sc.nextShort();
        sc.nextLine();
        
        return new Person(name, age);
    }
    //methods to hardcode collection & people
    public static void populateCollection(ArrayList<Media> col)
    {  
        col.add(new Novel("Goblet of Fire", "Bloomsbury Publishing", "Fantasy", true, new Queue(), "J. K. Rowling", 500)); //first Novel **reminder, to talk about the "new Queue()" tomorrow
        col.add(new Novel("The Lightning Thief", "Miramax Books", "Fantasy", true, new Queue(), "Rick Riordan", 700)); //Book 2
        col.add(new Audiobooks("Dune", "Holtzbrinck Publishing Group", "Fantasy", true, new Queue(), "Frank Herbert", 1200)); //AudioBook 1
        col.add(new VideoGames("It Takes Two", "Hazelight Studios", "Action", true, new Queue(), 13, "PS4")); //Game 1 **do we want to change the "rating" to a char?
        col.add(new Novel("Pride and Prejudice", "Modern Library", "Romance", true, new Queue(), "Jane Austen", 400)); //Book 3
        col.add(new Audiobooks("Outlander", "Recorded Books", "Fantasy", true, new Queue(), "Diana Gabaldon", 7420));
        col.add(new Audiobooks("One Plus One", "Penguin Audio", "Romance", true, new Queue(), "Jojo Moyes", 5600));
        col.add(new VideoGames("Red Dead Redemption 2", "Rockstar Games", "Action", true, new Queue(), 18, "XBox"));
        col.add(new Novel("Sherlock Holmes", "Bramhall House", "Mystery", true, new Queue(), "William S. Baring-Gould", 250));
        col.add(new VideoGames("Super Mario Bros", "Nintendo", "Platformer", true, new Queue(), 0, "Nintendo Switch"));
    }
    public static void populatePeople(ArrayList<Person> peop)
    {
        peop.add(new Person("Alex", (short) 10));
        peop.add(new Person("Bailey", (short) 15));
        peop.add(new Person("Charles", (short) 20));
    }
     
    // main program
    public static void main(String[] args) {
          
        
        Scanner sc = new Scanner(System.in);
        
        // pre-existing library, items, and people
        Library JMPL = new Library("John McCrae Public Library", "123 Internet Road");
        populateCollection(JMPL.collection);
        populatePeople(JMPL.people);
          
        // program loop        
        while (true) {
        
            // ask person for library account
            System.out.printf("Welcome to %s!\n\nDo you already have an account? (y/n) ", JMPL.name);
            String answer = sc.nextLine().toLowerCase();
            
            //repeat until valid answer is given
            while (!(answer.equals("y")) && !(answer.equals("n")))
            {
                System.out.print("Please answer 'y' or 'n': ");
                answer = sc.nextLine().toLowerCase();
            }
                
            // user has an account
            if (answer.equals("y")) {
                
                // library card number input
                System.out.print("\nGreat! Please enter your library card number: ");
                answer = sc.nextLine();
                
                //  no user with this card number found
                while (JMPL.findPerson(Integer.parseInt(answer)) == null) {
                    System.out.print("\nNo person was found under this card number, please try again. \nIf you need to create an account, type 'register': ");
                    answer = sc.nextLine().toLowerCase();
                    if (answer.equals("register")) {
                        //exit loop
                        break; 
                    }
                }
                // fetch user account
                if (!answer.equals("register"))
                    JMPL.currentUser = JMPL.findPerson(Integer.parseInt(answer));
            }
            
            // create user account
            if (answer.equals("n") || answer.equals("register")) {
                JMPL.currentUser = createPerson();
                JMPL.people.add(JMPL.currentUser);
                System.out.printf("\nYou account has been created:\n\n" + JMPL.currentUser + "\n");
            }
            
            // now everyone should be "logged in" and have an account
            do {
                
                // program option (inventory, chekout, or exit)
                System.out.print("\nWould you like to see your inventory ('current'), check out a new item ('search'), or exit the program ('exit')? "); //**this feels really weirdly worded, anyone can change this if they feel like it
                answer = sc.nextLine().toLowerCase();
                
                // error-catching
                while (!(answer.equals("current")) && !(answer.equals("search")) && !(answer.equals("exit")))
                {
                    System.out.print("Please enter a valid term: ");
                    answer = sc.nextLine().toLowerCase();
                }
            
                // access library
                if (answer.equals("search")) {
                     
                    // check if genre input is valid (if it exists)
                    boolean valid = false; 
                    
                    // ask whether user wants to filter search
                    System.out.println();
                    System.out.print("Would you like to search by filter? (y/n) ");
                    answer = sc.nextLine().toLowerCase();
                    
                    // error-catching
                    while (!(answer.equals("y")) && !(answer.equals("n")))
                    {
                        System.out.print("Please answer 'y' or 'n': ");
                        answer = sc.nextLine().toLowerCase();
                    }
                    //if they don't want a filtered search, print out the collection    
                    if (answer.equals("n"))
                    {
                        //print the entire collection, ask user to pick one, put in "checked out"
                        System.out.println();
                        JMPL.printCollection();
                    }
                    //if they do want a filtered search
                    else if(answer.equals("y"))
                    {
                        System.out.print("\nHow would you like to filter by 'genre' or by 'type': ");
                        answer = sc.nextLine().toLowerCase();
                        //looping until a valid answer is provided
                        while (!(answer.equals("genre")) && !(answer.equals("type")))
                        {
                            System.out.print("Please submit a valid answer('genre' or 'type'): ");
                            answer = sc.nextLine().toLowerCase();
                        }
                        //sort by genre
                        if (answer.equals("genre"))
                        {
                            System.out.print("What genre would you like to sort by? ");
                            answer = sc.nextLine();
                            System.out.println();
                            valid = JMPL.printFilteredCollection(answer);
                        }
                        //sort by type of media
                        else if(answer.equals("type"))
                        {
                            System.out.print(sortByMedia(JMPL.collection));
                        }
                    }
                    
                    // check genre input's validity
                    if(valid) {
                                
                        // ask for the item that the user wants to borrow
                        System.out.print("\nWhich item would you like to borrow? ");
                        answer = sc.nextLine();
                        
                        // error-catching (existing item or not)
                        while (JMPL.findMedia(answer) == null)
                        {
                            System.out.print("No item was found under this title, please try again: ");
                            answer = sc.nextLine();
                        }
                        
                        // checkout
                        JMPL.checkOut(JMPL.findMedia(answer), JMPL.currentUser); 
                        
                    }
                    
                }
                // see user's inventory
                else if (answer.equals("current"))
                {
                    
                    // output user's info
                    System.out.println("\n" + JMPL.currentUser);
                    
                    // check whether user can return items (whether they have one/some)
                    if (!JMPL.currentUser.possessions.isEmpty()) 
                    {
                        System.out.print("\nWould you like to return one of your items? (y/n) ");
                        answer = sc.nextLine().toLowerCase();
                        
                        while (!(answer.equals("y")) && !(answer.equals("n")))
                        {
                            System.out.print("Please enter 'y' or 'n': ");
                            answer = sc.nextLine().toLowerCase();
                        }
                        
                        if (answer.equals("y"))
                        {
                            for (int i = 0; i < JMPL.currentUser.possessions.size(); i++)
                            {
                                System.out.println("\nItem " + (i + 1) + ":\n\n" + JMPL.currentUser.possessions.get(i));
                            }
                            
                            System.out.print("\nPlease enter the number that corresponds with the item you would like to return: ");;
                            int answerNum = sc.nextInt();
                            sc.nextLine(); //scanner bug
                            
                            while (answerNum < 0 || answerNum > JMPL.currentUser.possessions.size())
                            {
                                System.out.print("Please enter a valid number: ");
                                answerNum = sc.nextInt();
                                sc.nextLine();
                            }
                            
                            JMPL.returnBook(JMPL.currentUser.possessions.get(answerNum - 1), JMPL.currentUser); //returns item
                        }
                        else
                        {
                            System.out.println("\nCome back to return items later.");
                        }
                    }
                    else
                    {
                        System.out.println("\nYou have no items currently checked out.");
                    }
                }
                
            } while (!(answer.equals("exit")));
            
            // proceed to next user
            System.out.print("\nNext person? (y/n) ");
            if (sc.nextLine().toLowerCase().equals("n")) 
                break;
            else 
                System.out.println();
            
        }
    }
}
