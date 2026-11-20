package DateBase;
import model.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class SaveCharger {
	


	    private static final String DATA_DIR = "data/";
	    private static final String USERS_FILE = DATA_DIR + "users.ser";
	    private static final String COURSES_FILE = DATA_DIR + "courses.ser";

	    // Sauvegarder tous les utilisateurs
	    public static void saveUsers(List<User> users) {
	        createDataDirIfNotExists();
	        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
	            oos.writeObject(users);
	            System.out.println("✅ Utilisateurs sauvegardés dans " + USERS_FILE);
	        } catch (IOException e) {
	            System.err.println("❌ Erreur lors de la sauvegarde : " + e.getMessage());
	        }
	    }

	    // Charger tous les utilisateurs
	    @SuppressWarnings("unchecked")
		public static List<User> loadUsers() {
	        createDataDirIfNotExists();
	        File file = new File(USERS_FILE);
	        if (!file.exists()) return new ArrayList<>(); // Fichier inexistant → retourne une liste vide

	        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
	            return (List<User>) ois.readObject();
	        } catch (IOException | ClassNotFoundException e) {
	            System.err.println("❌ Erreur lors du chargement : " + e.getMessage());
	            return new ArrayList<>();
	        }
	    }
	    public static void addUser(User newUser) {
	        List<User> users = loadUsers();
	        
	        // Vérifier si le matricule existe déjà
	        boolean exists = users.stream()
	                             .anyMatch(u -> u.getMatricule().equals(newUser.getMatricule()));
	        
	        if (!exists) {
	            users.add(newUser);
	            saveUsers(users);
	        } else {
	            System.err.println("❌ Utilisateur avec le matricule " + newUser.getMatricule() + " existe déjà !");
	        }}
	    /*
	      Supprime un utilisateur par son matricule.
	      matricule Le matricule de l'utilisateur à supprimer
	     return true si suppression réussie, false sinon
	     */
	    public static boolean suppUser(String matricule) {
	        try {
	            // 1. Charger tous les utilisateurs
	            List<User> users = loadUsers();
	            
	            // 2. Rechercher l'utilisateur
	            boolean removed = users.removeIf(u -> u.getMatricule().equals(matricule));
	            
	            if (removed) {
	                // 3. Sauvegarder la liste mise à jour
	                saveUsers(users);
	                System.out.println("✅ Utilisateur " + matricule + " supprimé !");
	                return true;
	            } else {
	                System.out.println("❌ Utilisateur " + matricule + " non trouvé !");
	                return false;
	            }
	        } catch (Exception e) {
	            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
	            return false;
	        }
	    }
	    public static void saveCourse(List<Course> courses) {
	        createDataDirIfNotExists();
	        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(COURSES_FILE))) {
	            oos.writeObject(courses);
	            System.out.println("✅ Courses sauvegardés dans " + COURSES_FILE);
	        } catch (IOException e) {
	            System.err.println("❌ Erreur lors de la sauvegarde : " + e.getMessage());
	        }
	    }

	    // Charger tous les utilisateurs
	    @SuppressWarnings("unchecked")
		public static List<Course> loadCourse() {
	        createDataDirIfNotExists();
	        File file = new File(COURSES_FILE);
	        if (!file.exists()) return new ArrayList<>(); // Fichier inexistant → retourne une liste vide

	        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
	            return (List<Course>) ois.readObject();
	        } catch (IOException | ClassNotFoundException e) {
	            System.err.println("❌ Erreur lors du chargement : " + e.getMessage());
	            return new ArrayList<>();
	        }
	    }
	    public static void addCourse(Course newCourse) {
	        List<Course> courses;
	        try {
	            courses = loadCourse();
	        } catch (Exception e) {
	            courses = new ArrayList<>();
	        }

	        boolean exists = courses.stream().anyMatch(c -> 
	            c.getConducteur().getMatricule().equals(newCourse.getConducteur().getMatricule()) &&
	            c.getDateTimeDepart().equals(newCourse.getDateTimeDepart()) &&
	            c.getTrajet().getEndroit().equals(newCourse.getTrajet().getEndroit())
	        );

	        if (exists) {
	            System.out.println("❌ Course existe déjà pour ce conducteur à cette heure !");
	            return;
	        }

	        courses.add(newCourse);
	        saveCourse(courses);
	        System.out.println("✅ Course ajoutée !");
	    }
	    /*
	      Supprime une course basée sur le conducteur, la date et le trajet.
	     conducteur Conducteur de la course
	    dateHeure Date et heure de départ
	      trajet Itinéraire de la course
	     return true si suppression réussie, false sinon
	     */
	    public static boolean suppCourse(User conducteur, LocalDateTime dateHeure, Itineraire trajet) {
	        try {
	            // 1. Charger toutes les courses
	            List<Course> courses = loadCourse();
	            
	            // 2. Supprimer la course correspondante
	            boolean removed = courses.removeIf(c -> 
	                c.getConducteur().equals(conducteur) &&
	                c.getDateTimeDepart().equals(dateHeure) &&
	                c.getTrajet().equals(trajet)
	            );
	            
	            if (removed) {
	                // 3. Sauvegarder la liste mise à jour
	                saveCourse(courses);
	                System.out.println("✅ Course supprimée !");
	                return true;
	            } else {
	                System.out.println("❌ Course non trouvée !");
	                return false;
	            }
	        } catch (Exception e) {
	            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
	            return false;
	        }
	    }
	    public static boolean suppCourseParId(long id) {
	        List<Course> courses = loadCourse();
	        boolean removed = courses.removeIf(c -> c.getId() == id);
	        if (removed) saveCourse(courses);
	        return removed;
	    }

	    

	    private static void createDataDirIfNotExists() {
	        File dir = new File(DATA_DIR);
	        if (!dir.exists()) dir.mkdir(); // Crée le dossier "data/" s'il n'existe pas
	    }
	    
	    
	    
	    public static void clearAllData() {
	        new File(USERS_FILE).delete();
	        new File(COURSES_FILE).delete();
	        createDataDirIfNotExists();
	    }
	    
	    
	    
	    
	    
	    
	    
	    
	}
