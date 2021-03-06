package fi.gosu.ika.controller;

import fi.gosu.ika.domain.Kulttuuri;
import fi.gosu.ika.repository.KulttuuriRepository;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/*")
public class DefaultController {

    @Autowired
    private KulttuuriRepository kulttuuriRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String view(Model model) {
        List<Kulttuuri> kulttuurit = kulttuuriRepository.findAll();
        Collections.sort(kulttuurit, new Comparator<Kulttuuri>() {
            @Override
            public int compare(Kulttuuri o1, Kulttuuri o2) {
                return o1.getTime().compareTo(o2.getTime()) * -1;
            }
        });
        model.addAttribute("kulttuurit", kulttuuriRepository.findAll());
        return "index";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createKulttuuri(Kulttuuri kulttuuri) {
        kulttuuriRepository.save(kulttuuri);
        return "redirect:";
    }

    @RequestMapping(value = "kulttuuri/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id) {
        kulttuuriRepository.delete(kulttuuriRepository.findOne(id));
        return new ResponseEntity(HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "kulttuuri/{id}/change", method = RequestMethod.POST)
    public ResponseEntity change(@PathVariable Long id, @RequestParam int count) {
        Kulttuuri kulttuuri = kulttuuriRepository.findOne(id);
        kulttuuri.setKpl(count);
        kulttuuri.setTime(new Date(System.currentTimeMillis()));
        return new ResponseEntity(kulttuuri, HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = "kulttuuri/{id}/changenotime", method = RequestMethod.POST)
    public ResponseEntity changeNoTime(@PathVariable Long id, @RequestParam int count) {
        Kulttuuri kulttuuri = kulttuuriRepository.findOne(id);
        kulttuuri.setKpl(count);
        return new ResponseEntity(kulttuuri, HttpStatus.OK);
    }

}
