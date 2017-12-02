package shine.com.basicrxjava.entity;

/**
 * Created by Administrator on 2016/7/22.
 */
public class Student {
    private String name;
    private int age;
    private Course[] mCourses=new Course[3];

    public Student() {

    }

    public Student(String name, int age) {
        this.name = name;
        this.age = age;
        for (int i = 0; i < mCourses.length; i++) {
            mCourses[i]=new Course(age);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Course[] getCourses() {
        return mCourses;
    }

    public  static class Course {
        private String name;


        public Course(int age) {
            this.name = "lxl"+age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    }
}
