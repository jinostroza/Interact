// Add-On Code: Item Bullets (CSS - Imageless)
 
    /* <![CDATA[ */
    qmad.br_navigator = navigator.userAgent.indexOf("Netscape") + 1;
    qmad.br_version = parseFloat(navigator.vendorSub);
    qmad.br_oldnav6 = qmad.br_navigator && qmad.br_version < 7;
    qmad.br_strict = (dcm = document.compatMode) && dcm == "CSS1Compat";
    qmad.br_ie = window.showHelp;
    qmad.str = (qmad.br_ie && !qmad.br_strict);
    if (!qmad.br_oldnav6) {
        if (!qmad.ibcss) qmad.ibcss = new Object();
        if (qmad.bvis.indexOf("qm_ibcss_active(o,false);") == -1) {
            qmad.bvis += "qm_ibcss_active(o,false);";
            qmad.bhide += "qm_ibcss_active(a,1);";
            if (window.attachEvent) window.attachEvent("onload", qm_ibcss_init);
            else if (window.addEventListener) window.addEventListener("load", qm_ibcss_init, 1);
            if (window.attachEvent) document.attachEvent("onmouseover", qm_ibcss_hover_off);
            else if (window.addEventListener) document.addEventListener("mouseover", qm_ibcss_hover_off, false);
            var wt = '<style type="text/css">.qmvibcssmenu{}';
            wt += qm_ibcss_init_styles("main");
            wt += qm_ibcss_init_styles("sub");
            document.write(wt + '</style>');
        }
    };

    function qm_ibcss_init_styles(pfix, id) {
        var wt = '';
        var a = "#ffffff";
        var b = "#000000";
        var t, q;
        add_div = "";
        if (pfix == "sub") add_div = "div ";
        var r1 = "ibcss_" + pfix + "_bg_color";
        var r2 = "ibcss_" + pfix + "_border_color";
        for (var i = 0; i < 10; i++) {
            if (q = qmad["qm" + i]) {
                if (t = q[r1]) a = t;
                if (t = q[r2]) b = t;
                wt += '#qm' + i + ' ' + add_div + '.qm-ibcss-static span{background-color:' + a + ';border-color:' + b + ';}';
                if (t = q[r1 + "_hover"]) a = t;
                if (t = q[r2 + "_hover"]) b = t;
                wt += '#qm' + i + '  ' + add_div + '.qm-ibcss-hover span{background-color:' + a + ';border-color:' + b + ';}';
                if (t = q[r1 + "_active"]) a = t;
                if (t = q[r2 + "_active"]) b = t;
                wt += '#qm' + i + '  ' + add_div + '.qm-ibcss-active span{background-color:' + a + ';border-color:' + b + ';}';
            }
        }
        return wt;
    };

    function qm_ibcss_init(e, spec) {
        var z;
        if ((z = window.qmv) && (z = z.addons) && (z = z.ibcss) && (!z["on" + qmv.id] && z["on" + qmv.id] != undefined && z["on" + qmv.id] != null)) return;
        qm_ts = 1;
        var q = qmad.ibcss;
        var a, b, r, sx, sy;
        z = window.qmv;
        for (i = 0; i < 10; i++) {
            if (!(a = document.getElementById("qm" + i)) || (!isNaN(spec) && spec != i)) continue;
            var ss = qmad[a.id];
            if (ss && (ss.ibcss_main_type || ss.ibcss_sub_type)) {
                q.mtype = ss.ibcss_main_type;
                q.msize = ss.ibcss_main_size;
                if (!q.msize) q.msize = 5;
                q.md = ss.ibcss_main_direction;
                if (!q.md) md = "right";
                q.mbg = ss.ibcss_main_bg_color;
                q.mborder = ss.ibcss_main_border_color;
                sx = ss.ibcss_main_position_x;
                sy = ss.ibcss_main_position_y;
                if (!sx) sx = 0;
                if (!sy) sy = 0;
                q.mpos = eval("new Array('" + sx + "','" + sy + "')");
                q.malign = eval("new Array('" + ss.ibcss_main_align_x + "','" + ss.ibcss_main_align_y + "')");
                r = q.malign;
                if (!r[0]) r[0] = "right";
                if (!r[1]) r[1] = "center";
                q.stype = ss.ibcss_sub_type;
                q.ssize = ss.ibcss_sub_size;
                if (!q.ssize) q.ssize = 5;
                q.sd = ss.ibcss_sub_direction;
                if (!q.sd) sd = "right";
                q.sbg = ss.ibcss_sub_bg_color;
                q.sborder = ss.ibcss_sub_border_color;
                sx = ss.ibcss_sub_position_x;
                sy = ss.ibcss_sub_position_y;
                if (!sx) sx = 0;
                if (!sy) sy = 0;
                q.spos = eval("new Array('" + sx + "','" + sy + "')");
                q.salign = eval("new Array('" + ss.ibcss_sub_align_x + "','" + ss.ibcss_sub_align_y + "')");
                r = q.salign;
                if (!r[0]) r[0] = "right";
                if (!r[1]) r[1] = "middle";
                q.type = ss.ibcss_apply_to;
                qm_ibcss_create_inner("m");
                qm_ibcss_create_inner("s");
                qm_ibcss_init_items(a, 1, "qm" + i);
            }
        }
    };

    function qm_ibcss_create_inner(pfix) {
        var q = qmad.ibcss;
        var wt = "";
        var s = q[pfix + "size"];
        var type = q[pfix + "type"];
        var head;
        if (type.indexOf("head") + 1) head = true;
        var gap;
        if (type.indexOf("gap") + 1) gap = true;
        var v;
        if (type.indexOf("-v") + 1) v = true;
        if (type.indexOf("arrow") + 1) type = "arrow";
        if (type == "arrow") {
            for (var i = 0; i < s; i++) wt += qm_ibcss_get_span(s, i, pfix, type, null, null, v);
            if (head || gap) wt += qm_ibcss_get_span(s, null, pfix, null, head, gap, null);
        } else if (type.indexOf("square") + 1) {
            var inner;
            if (type.indexOf("-inner") + 1) inner = true;
            var raised;
            if (type.indexOf("-raised") + 1) raised = true;
            type = "square";
            for (var i = 0; i < 3; i++) wt += qm_ibcss_get_span(s, i, pfix, type, null, null, null, inner, raised);
            if (inner) wt += qm_ibcss_get_span(s, i, pfix, "inner");
        }
        q[pfix + "inner"] = wt;
    };

    function qm_ibcss_get_span(size, i, pfix, type, head, gap, v, trans, raised) {
        var q = qmad.ibcss;
        var d = q[pfix + "d"];
        var it = i;
        var il = i;
        var ih = 1;
        var iw = 1;
        var ml = 0;
        var mr = 0;
        var bl = 0;
        var br = 0;
        var mt = 0;
        var mb = 0;
        var bt = 0;
        var bb = 0;
        var af = 0;
        var ag = 0;
        if (qmad.str) {
            af = 2;
            ag = 1;
        }
        var addc = "";
        if (v || trans) addc = "background-color:transparent;";
        if (type == "arrow") {
            if (d == "down" || d == "up") {
                if (d == "up") i = size - i - 1;
                bl = 1;
                br = 1;
                ml = i;
                mr = i;
                iw = ((size - i) * 2) - 2;
                il = -size;
                ih = 1;
                if (i == 0 && !v) {
                    bl = iw + 2;
                    br = 0;
                    ml = 0;
                    mr = 0;
                    iw = 0;
                    if (qmad.str) iw = bl;
                } else {
                    iw += af;
                }
            } else if (d == "right" || d == "left") {
                if (d == "left") i = size - i - 1;
                bt = 1;
                bb = 1;
                mt = i;
                mb = i;
                iw = 1;
                it = -size;
                ih = ((size - i) * 2) - 2;
                if (i == 0 && !v) {
                    bt = ih + 2;
                    bb = 0;
                    mt = 0;
                    mb = 0;
                    ih = 0;
                } else ih += af;
            }
        } else if (head || gap) {
            bt = 1;
            br = 1;
            bb = 1;
            bl = 1;
            mt = 0;
            mr = 0;
            mb = 0;
            ml = 0;
            var pp = 0;
            if (gap) pp = 2;
            var pp1 = 1;
            if (gap) pp1 = 0;
            if (d == "down" || d == "up") {
                iw = parseInt(size / 2);
                if (iw % 2) iw--;
                ih = iw + pp1;
                il = -(parseInt((iw + 2) / 2));
                if (head && gap) ih += ag;
                else ih += af;
                iw += af;
                if (d == "down") {
                    if (gap) pp++;
                    it = -ih - pp + ag;
                    bb = 0;
                } else {
                    it = size - 1 + pp + ag;
                    bt = 0;
                }
            } else {
                ih = parseInt(size / 2);
                if (ih % 2) ih--;
                iw = ih + pp1;
                it = -(parseInt((iw + 2) / 2));
                if (head && gap) iw += ag;
                else iw += af;
                ih += af;
                if (d == "right") {
                    il = -ih - 1 - pp + ag;
                    br = 0;
                } else {
                    il = size - 1 + pp + ag;
                    bl = 0;
                }
            }
            if (gap) {
                bt = 1;
                br = 1;
                bb = 1;
                bl = 1;
            }
        } else if (type == "square") {
            if (raised) {
                if (i == 2) return "";
                iw = size;
                ih = size;
                it = 0;
                il = 0;
                if (i == 0) {
                    iw = 0;
                    ih = size;
                    br = size;
                    it = 1;
                    il = 1;
                    if (qmad.str) iw = br;
                }
            } else {
                if (size % 2) size++;
                it = 1;
                ih = size;
                iw = size;
                bl = 1;
                br = 1;
                il = 0;
                iw += af;
                if (i == 0 || i == 2) {
                    ml = 1;
                    it = 0;
                    ih = 1;
                    bl = size;
                    br = 0;
                    iw = 0;
                    if (qmad.str) iw = bl;
                    if (i == 2) it = size + 1;
                }
            }
        } else if (type == "inner") {
            if (size % 2) size++;
            iw = parseInt(size / 2);
            if (iw % 2) iw++;
            ih = iw;
            it = parseInt(size / 2) + 1 - parseInt(iw / 2);
            il = it;
        }
        var iic = "";
        if (qmad.str) iic = "&nbsp;";
        return '<span style="' + addc + 'border-width:' + bt + 'px ' + br + 'px ' + bb + 'px ' + bl + 'px;border-style:solid;display:block;position:absolute;overflow:hidden;font-size:1px;line-height:0px;height:' + ih + 'px;margin:' + mt + 'px ' + mr + 'px ' + mb + 'px ' + ml + 'px;width:' + iw + 'px;top:' + it + 'px;left:' + il + 'px;">' + iic + '</span>';
    };

    function qm_ibcss_init_items(a, main) {
        var q = qmad.ibcss;
        var aa, pf;
        aa = a.childNodes;
        for (var j = 0; j < aa.length; j++) {
            if (aa[j].tagName == "A") {
                if (window.attachEvent) aa[j].attachEvent("onmouseover", qm_ibcss_hover);
                else if (window.addEventListener) aa[j].addEventListener("mouseover", qm_ibcss_hover, false);
                var skip = false;
                if (q.type != "all") {
                    if (q.type == "parent" && !aa[j].cdiv) skip = true;
                    if (q.type == "non-parent" && aa[j].cdiv) skip = true;
                }
                if (!skip) {
                    if (main) pf = "m";
                    else pf = "s";
                    var ss = document.createElement("SPAN");
                    ss.className = "qm-ibcss-static";
                    var s1 = ss.style;
                    s1.display = "block";
                    s1.position = "relative";
                    s1.fontSize = "1px";
                    s1.lineHeight = "0px";
                    s1.zIndex = 1;
                    ss.ibhalign = q[pf + "align"][0];
                    ss.ibvalign = q[pf + "align"][1];
                    ss.ibposx = q[pf + "pos"][0];
                    ss.ibposy = q[pf + "pos"][1];
                    ss.ibsize = q[pf + "size"];
                    qm_ibcss_position(aa[j], ss);
                    ss.innerHTML = q[pf + "inner"];
                    aa[j].qmibulletcss = aa[j].insertBefore(ss, aa[j].firstChild);
                    ss.setAttribute("qmvbefore", 1);
                    ss.setAttribute("isibulletcss", 1);
                    if (aa[j].className.indexOf("qmactive") + 1) qm_ibcss_active(aa[j]);
                }
                if (aa[j].cdiv) new qm_ibcss_init_items(aa[j].cdiv, null);
            }
        }
    };

    function qm_ibcss_position(a, b) {
        if (b.ibhalign == "right") b.style.left = (a.offsetWidth + parseInt(b.ibposx) - b.ibsize) + "px";
        else if (b.ibhalign == "center") b.style.left = (parseInt(a.offsetWidth / 2) - parseInt(b.ibsize / 2) + parseInt(b.ibposx)) + "px";
        else b.style.left = b.ibposx + "px";
        if (b.ibvalign == "bottom") b.style.top = (a.offsetHeight + parseInt(b.ibposy) - b.ibsize) + "px";
        else if (b.ibvalign == "middle") b.style.top = parseInt((a.offsetHeight / 2) - parseInt(b.ibsize / 2) + parseInt(b.ibposy)) + "px";
        else b.style.top = b.ibposy + "px";
    };

    function qm_ibcss_hover(e, targ) {
        e = e || window.event;
        if (!targ) {
            var targ = e.srcElement || e.target;
            while (targ.tagName != "A") targ = targ[qp];
        }
        var ch = qmad.ibcss.lasth;
        if (ch && ch != targ && ch.qmibulletcss) qm_ibcss_hover_off(new Object(), ch);
        if (targ.className.indexOf("qmactive") + 1) return;
        var wo = targ.qmibulletcss;
        if (wo) {
            x2("qm-ibcss-hover", wo, 1);
            qmad.ibcss.lasth = targ;
        }
        if (e) qm_kille(e);
    };

    function qm_ibcss_hover_off(e, o) {
        if (!o) o = qmad.ibcss.lasth;
        if (o && o.qmibulletcss) x2("qm-ibcss-hover", o.qmibulletcss);
    };

    function qm_ibcss_active(a, hide) {
        if (!hide && a.className.indexOf("qmactive") == -1) return;
        if (hide && a.idiv) {
            var o = a.idiv;
            if (o && o.qmibulletcss) {
                x2("qm-ibcss-active", o.qmibulletcss);
            }
        } else {
            if (!a.cdiv.offsetWidth) a.cdiv.style.visibility = "inherit";
            qm_ibcss_wait_relative(a);
            var wo = a.qmibulletcss;
            if (wo) x2("qm-ibcss-active", wo, 1);
        }
    };

    function qm_ibcss_wait_relative(a) {
        if (!a) a = qmad.ibcss.cura;
        if (a.cdiv) {
            if (a.cdiv.qmtree && a.cdiv.style.position != "relative") {
                qmad.ibcss.cura = a;
                setTimeout("qm_ibcss_wait_relative()", 10);
                return;
            }
            var aa = a.cdiv.childNodes;
            for (var i = 0; i < aa.length; i++) {
                if (aa[i].tagName == "A" && aa[i].qmibulletcss) qm_ibcss_position(aa[i], aa[i].qmibulletcss);
            }
        }
    } /* ]]> */ 