package com.app.stats;

import com.app.appUser.AppUser;
import com.app.appUser.UserService;
import com.app.category.Category;
import com.app.category.CategoryService;
import com.app.system.Result;
import com.app.system.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static com.app.util.Global.ADMIN;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
@Secured({ADMIN})
public class StatsController {

    private final CategoryService categoryService;
    private final UserService userService;

    @GetMapping("/categories")
    public Result categories() {
        Map<String, List<?>> res = new HashMap<>();

        List<Category> categories = categoryService.findAll();

        List<String> names = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (Category category : categories) {
            names.add(category.getName());
            values.add(category.getUsers().size());
        }

        res.put("names", names);
        res.put("values", values);

        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Categories",
                Collections.unmodifiableMap(res)
        );
    }

    @GetMapping("/intensity")
    public Result intensity() {
        Map<String, List<?>> res = new HashMap<>();

        List<AppUser> users = userService.findAllByRoleUser();

        users.sort(Comparator.comparing(AppUser::getTasksIntensity));
        Collections.reverse(users);

        List<String> names = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (int i = 0; i < users.size(); i++) {
            if (i == 5) break;

            names.add(users.get(i).getFio());
            values.add(users.get(i).getTasksIntensity());
        }

        res.put("names", names);
        res.put("values", values);

        return new Result(
                true,
                StatusCode.SUCCESS,
                "Success Intensity",
                Collections.unmodifiableMap(res)
        );
    }


}
