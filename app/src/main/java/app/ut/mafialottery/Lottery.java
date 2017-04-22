package app.ut.mafialottery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Lottery {
    public static final String MAFIA_NAME = "مافیا";
    public static final String CITIZEN_NAME = "شهروند";
    private List<Role> roles;

    public Lottery(int mafiaCount, int citizenCount) {
        roles = new ArrayList<>();
        roles.add(new Role(MAFIA_NAME, mafiaCount));
        roles.add(new Role(CITIZEN_NAME, citizenCount));
    }

    public Lottery() {
        roles = new ArrayList<>();
        roles.add(new Role(MAFIA_NAME, 0));
        roles.add(new Role(CITIZEN_NAME, 0));
    }

    private int findRole(String name) {
        for(int i = 0; i < roles.size(); i++) {
            if(roles.get(i).getName().equals(name)) {
                return i;
            }
        }

        return -1;
    }

    private void updateRole(int index) {
        roles.get(index).decrementCount();
        if(roles.get(index).getCount() == 0)
            roles.remove(index);
    }

    public int getPlayerCount() {
        int count = 0;
        for(Role r: roles)
            count += r.getCount();
        return count;
    }

    public void updateRoleCount(String name, int count) {
        int index = findRole(name);
        if(index != -1) {
            roles.get(index).setCount(count);
        }
    }

    public boolean addNewRole(Role role) {
        if(findRole(role.getName()) != -1)
            return false;

        roles.add(role);
        return true;
    }

    public void removeRole(String name) {
        int index = findRole(name);
        roles.remove(index);
    }

    public boolean roleLeft() {
        return roles.size() != 0;
    }

    public String getRandomRole() {
        if(roles.size() == 0)
            return "";
        Random rand = new Random(System.currentTimeMillis());
        int randomRoleIndex = rand.nextInt(roles.size());
        String roleName = roles.get(randomRoleIndex).getName();
        updateRole(randomRoleIndex);
        return roleName;
    }
}
