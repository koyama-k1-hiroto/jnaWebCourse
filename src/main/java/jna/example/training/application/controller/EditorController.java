package jna.example.training.application.controller;

import jna.example.training.application.resource.EditorRequest;
import jna.example.training.application.resource.EditorResponseResource;
import jna.example.training.domain.object.EmpNo;
import jna.example.training.domain.service.RegisterService;
import jna.example.training.domain.service.ViewerService;
import jna.example.training.infrastructure.entity.PrefecturesEntity;
import jna.example.training.infrastructure.entity.SexEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

@Controller
@AllArgsConstructor
public class EditorController {

    protected final MessageSource messageSource;
    private final RegisterService registerService;
    private final ViewerService viewerService;

    /*
        画面から受け取る値が未入力、未設定ならnullを設定するためのメソッド
     */
    @InitBinder
    public void initbinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/editor/{empNo}")
    public String init(@Validated @PathVariable(name = "empNo") @Pattern(regexp = "[0-9]{6}") String empNo,
                       @ModelAttribute EditorRequest editorRequest,
                       Model model) {

        EditorResponseResource resource = viewerService.searchById(EmpNo.of(empNo));
        editorRequest.factory(resource);

        return "editor";
    }

    @PostMapping("/editor")
    public String editor(@Validated @ModelAttribute EditorRequest editorRequest,
                         BindingResult result,
                         Model model) {
        if (result.hasErrors()) {
            return "editor";
        }

        // TODO:更新処理


        model.addAttribute("complete", messageSource.getMessage("editor.complete", null, Locale.JAPAN));

        return "editor";
    }

    @ModelAttribute("sexItemList")
    public List<SexEntity> sexItemList() {
        return registerService.getSexList();
    }

    @ModelAttribute("birthPlaceList")
    public List<PrefecturesEntity> birthPlaceList() {
        return registerService.getPrefecturesList();
    }

    @ModelAttribute("assigneeList")
    public HashMap<String, String> assigneeList() {
        return new LinkedHashMap<String, String>() {{
            put("1", "神奈川営業所");
            put("2", "新宿営業所");
            put("3", "大阪営業所");
            put("4", "福岡営業所");
            put("5", "仙台営業所");
            put("6", "宇都宮営業所");
            put("7", "大宮営業所");
            put("8", "名古屋営業所");
        }};
    }

}