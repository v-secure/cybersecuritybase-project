package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Signup;
import sec.project.repository.SignupRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;

    @RequestMapping("*")
    public String defaultMapping() {
        return "redirect:/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String loadForm(Model model) throws SQLException {
        List<Signup> signups = new ArrayList<>();
        Connection connection = DriverManager.getConnection("jdbc:h2:file:./database", "sa", "");
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM Signup");

        while (resultSet.next()) {
            Signup signup = new Signup(resultSet.getString("name"), resultSet.getString("address"));
            System.out.println(resultSet.getString("id") + "\t" + resultSet.getString("name")
                    + "\t" + resultSet.getString("address"));
            signups.add(signup);
        }

        resultSet.close();
        connection.close();

        model.addAttribute("list", signups);
        return "form";
    }

    //fixme use the proper HTTP methods for anything that modifies state (PATCH, POST, PUT, and DELETE – not GET)
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String submitForm(@RequestParam String name, @RequestParam String address) {
        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:h2:file:./database", "sa", "");
            PreparedStatement statement = connection.prepareStatement("INSERT INTO Signup (id, name, address) VALUES (?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, name);
            statement.setString(3, address);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/form";
    }

}
