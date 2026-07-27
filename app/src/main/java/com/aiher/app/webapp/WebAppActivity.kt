package com.aiher.app.webapp

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.aiher.app.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import android.view.ViewGroup

data class WebAppTemplate(
    val name: String,
    val description: String,
    val htmlPreview: String,
    val category: String
)

@AndroidEntryPoint
class WebAppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIHerTheme {
                WebAppScreen(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebAppScreen(onBack: () -> Unit) {
    var selectedTemplate by remember { mutableStateOf<WebAppTemplate?>(null) }

    val templates = remember {
        listOf(
            WebAppTemplate(
                "低多边形森林探索",
                "3D低多边形风格森林场景，支持自由探索",
                htmlDemoForest(),
                "3D 场景"
            ),
            WebAppTemplate(
                "3D山地自由视角",
                "3D山地地形，支持旋转缩放查看",
                htmlDemoMountain(),
                "3D 场景"
            ),
            WebAppTemplate(
                "粒子时钟",
                "炫酷的粒子效果数字时钟",
                htmlDemoClock(),
                "动画效果"
            ),
            WebAppTemplate(
                "贪吃蛇游戏",
                "经典贪吃蛇Web版",
                htmlDemoSnake(),
                "游戏"
            ),
            WebAppTemplate(
                "数据可视化看板",
                "实时数据图表展示",
                htmlDemoDashboard(),
                "工具"
            ),
            WebAppTemplate(
                "Markdown编辑器",
                "实时预览的Markdown编辑器",
                htmlDemoMarkdown(),
                "工具"
            ),
        )
    }

    val selected = selectedTemplate
    if (selected != null) {
        // WebView预览
        var webViewRef by remember { mutableStateOf<WebView?>(null) }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(selected.name, color = TextOnPrimary, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { selectedTemplate = null }) {
                            Icon(Icons.Filled.ArrowBack, "返回", tint = TextOnPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple500)
                )
            }
        ) { padding ->
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.allowFileAccess = true
                        settings.allowContentAccess = true
                        settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        settings.cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
                        settings.databaseEnabled = true
                        webViewClient = WebViewClient()
                        webChromeClient = WebChromeClient()
                        loadDataWithBaseURL(null, selected.htmlPreview, "text/html", "UTF-8", null)
                        webViewRef = this
                    }
                },
                update = { webview ->
                    if (webview.url == null) {
                        webview.loadDataWithBaseURL(null, selected.htmlPreview, "text/html", "UTF-8", null)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
            DisposableEffect(selected) {
                onDispose {
                    webViewRef?.let { wv ->
                        (wv.parent as? ViewGroup)?.removeView(wv)
                        wv.destroy()
                    }
                    webViewRef = null
                }
            }
        }
    } else {
        // 模板列表
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Web App", color = TextOnPrimary, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Filled.ArrowBack, "返回", tint = TextOnPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple500)
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(templates) { template ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        onClick = { selectedTemplate = template }
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Web, null, tint = Purple500, modifier = Modifier.size(32.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(template.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                                Text(template.description, fontSize = 13.sp, color = TextSecondary)
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Purple200,
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    Text(
                                        template.category,
                                        fontSize = 11.sp,
                                        color = Purple700,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Icon(Icons.Filled.ChevronRight, null, tint = TextSecondary)
                        }
                    }
                }
            }
        }
    }
}

// ============ HTML Demo 内容 ============

private fun htmlDemoForest(): String = """
<!DOCTYPE html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<style>body{margin:0;overflow:hidden}canvas{display:block}</style>
</head><body>
<script src="https://cdn.jsdelivr.net/npm/three@0.160.0/build/three.min.js"></script>
<script>
const scene=new THREE.Scene();scene.background=new THREE.Color(0x87CEEB);scene.fog=new THREE.Fog(0x87CEEB,10,50);
const camera=new THREE.PerspectiveCamera(75,innerWidth/innerHeight,0.1,1000);
const renderer=new THREE.WebGLRenderer();renderer.setSize(innerWidth,innerHeight);document.body.appendChild(renderer.domElement);
camera.position.set(0,5,10);camera.lookAt(0,0,0);
const geo=new THREE.ConeGeometry(1,3,6);const mat=new THREE.MeshLambertMaterial({color:0x228B22});
for(let i=0;i<30;i++){const tree=new THREE.Mesh(geo,mat);tree.position.set((Math.random()-0.5)*30,1.5,(Math.random()-0.5)*30);scene.add(tree);}
const ground=new THREE.Mesh(new THREE.PlaneGeometry(50,50),new THREE.MeshLambertMaterial({color:0x567d46}));ground.rotation.x=-Math.PI/2;scene.add(ground);
const light=new THREE.DirectionalLight(0xffffff,1);light.position.set(5,10,5);scene.add(light);
const amb=new THREE.AmbientLight(0x404040,0.5);scene.add(amb);
let mx=0,my=0;addEventListener('mousemove',e=>{mx=e.clientX/innerWidth-0.5;my=e.clientY/innerHeight-0.5;});
function animate(){camera.position.x=mx*15;camera.position.z=10+my*5;camera.lookAt(0,0,0);renderer.render(scene,camera);requestAnimationFrame(animate);}
animate();addEventListener('resize',()=>{camera.aspect=innerWidth/innerHeight;camera.updateProjectionMatrix();renderer.setSize(innerWidth,innerHeight);});
</script></body></html>
""".trimIndent()

private fun htmlDemoMountain(): String = """
<!DOCTYPE html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<style>body{margin:0;overflow:hidden}canvas{display:block}</style>
</head><body>
<script src="https://cdn.jsdelivr.net/npm/three@0.160.0/build/three.min.js"></script>
<script>
const scene=new THREE.Scene();scene.background=new THREE.Color(0x1a1a2e);
const camera=new THREE.PerspectiveCamera(60,innerWidth/innerHeight,0.1,1000);
const renderer=new THREE.WebGLRenderer();renderer.setSize(innerWidth,innerHeight);document.body.appendChild(renderer.domElement);
const vertices=[];const size=40,segs=40;
for(let y=0;y<=segs;y++)for(let x=0;x<=segs;x++){
const h=Math.sin(x*0.3)*Math.cos(y*0.3)*5+Math.sin(x*0.5+y*0.5)*3;
vertices.push(x-size/2,h,y-size/2);}
const geo=new THREE.PlaneGeometry(size,size,segs,segs);
geo.rotateX(-Math.PI/2);
const pos=geo.attributes.position;
for(let i=0;i<pos.count;i++){pos.setY(i,Math.sin(pos.getX(i)*0.3)*Math.cos(pos.getZ(i)*0.3)*5+Math.sin(pos.getX(i)*0.5+pos.getZ(i)*0.5)*3);}
geo.computeVertexNormals();
const mat=new THREE.MeshPhongMaterial({color:0x6C5CE7,flatShading:true,vertexColors:false});
const mesh=new THREE.Mesh(geo,mat);scene.add(mesh);
scene.add(new THREE.AmbientLight(0x404060,0.5));
const dir=new THREE.DirectionalLight(0xffffff,0.8);dir.position.set(10,20,10);scene.add(dir);
camera.position.set(0,15,25);camera.lookAt(0,0,0);
let rot=0;addEventListener('mousemove',e=>{rot=e.clientX/innerWidth*Math.PI*2;});
function animate(){camera.position.x=Math.sin(rot)*25;camera.position.z=Math.cos(rot)*25;camera.lookAt(0,0,0);renderer.render(scene,camera);requestAnimationFrame(animate);}
animate();
</script></body></html>
""".trimIndent()

private fun htmlDemoClock(): String = """
<!DOCTYPE html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<style>body{margin:0;background:#0a0a0a;overflow:hidden;display:flex;align-items:center;justify-content:center;height:100vh}
canvas{display:block}</style>
</head><body><canvas id="c"></canvas>
<script>
const c=document.getElementById('c');const ctx=c.getContext('2d');
function resize(){c.width=innerWidth;c.height=innerHeight;}resize();addEventListener('resize',resize);
const particles=[];
function timeStr(){const d=new Date();return d.getHours().toString().padStart(2,'0')+':'+d.getMinutes().toString().padStart(2,'0')+':'+d.getSeconds().toString().padStart(2,'0');}
function draw(){
ctx.fillStyle='rgba(10,10,10,0.1)';ctx.fillRect(0,0,c.width,c.height);
ctx.font='bold '+(c.height*0.2)+'px monospace';ctx.textAlign='center';ctx.textBaseline='middle';
const t=timeStr();ctx.fillStyle='rgba(108,92,231,0.15)';ctx.fillText(t,c.width/2,c.height/2);
const data=ctx.getImageData(0,0,c.width,c.height).data;
if(particles.length<300){particles.push({x:Math.random()*c.width,y:Math.random()*c.height,vx:0,vy:0});}
for(const p of particles){const ix=Math.floor(p.x),iy=Math.floor(p.y);if(ix>0&&ix<c.width&&iy>0&&iy<c.height){const idx=(iy*c.width+ix)*4;if(data[idx]>0){p.vx+=(Math.random()-0.5)*0.5;p.vy+=(Math.random()-0.5)*0.5;}else{p.vx+=0.02;p.vy-=0.05;}}
p.x+=p.vx;p.y+=p.vy;p.vx*=0.95;p.vy*=0.95;
if(p.x<0||p.x>c.width)p.vx*=-1;if(p.y<0||p.y>c.height)p.vy*=-1;
ctx.fillStyle='hsl('+(Date.now()/30%360)+',80%,60%)';ctx.fillRect(p.x,p.y,2,2);}
requestAnimationFrame(draw);}
draw();
</script></body></html>
""".trimIndent()

private fun htmlDemoSnake(): String = """
<!DOCTYPE html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<style>body{margin:0;background:#1a1a2e;display:flex;flex-direction:column;align-items:center;justify-content:center;height:100vh}
canvas{border:2px solid #6C5CE7;border-radius:8px}#s{color:#A29BFE;font-family:monospace;margin:10px}</style>
</head><body><div id="s">Score: 0</div><canvas id="c"></canvas>
<script>
const c=document.getElementById('c'),ctx=c.getContext('2d');c.width=300;c.height=300;
let s=20,g=15,snake=[{x:10,y:10}],dir={x:1,y:0},food={x:5,y:5},score=0;
addEventListener('keydown',e=>{if(e.key=='ArrowUp'&&dir.y==0)dir={x:0,y:-1};if(e.key=='ArrowDown'&&dir.y==0)dir={x:0,y:1};if(e.key=='ArrowLeft'&&dir.x==0)dir={x:-1,y:0};if(e.key=='ArrowRight'&&dir.x==0)dir={x:1,y:0};});
let touchX,touchY;c.addEventListener('touchstart',e=>{touchX=e.touches[0].clientX;touchY=e.touches[0].clientY;});
c.addEventListener('touchend',e=>{const dx=e.changedTouches[0].clientX-touchX,dy=e.changedTouches[0].clientY-touchY;if(Math.abs(dx)>Math.abs(dy)){if(dx>0&&dir.x==0)dir={x:1,y:0};if(dx<0&&dir.x==0)dir={x:-1,y:0};}else{if(dy>0&&dir.y==0)dir={x:0,y:1};if(dy<0&&dir.y==0)dir={x:0,y:-1};}});
function update(){const h={x:snake[0].x+dir.x,y:snake[0].y+dir.y};
if(h.x<0||h.x>=20||h.y<0||h.y>=20||snake.some(s=>s.x==h.x&&s.y==h.y)){snake=[{x:10,y:10}];dir={x:1,y:0};score=0;document.getElementById('s').textContent='Score: '+score;return;}
snake.unshift(h);if(h.x==food.x&&h.y==food.y){score++;document.getElementById('s').textContent='Score: '+score;food={x:Math.floor(Math.random()*20),y:Math.floor(Math.random()*20)};}else snake.pop();}
function draw(){ctx.fillStyle='#1a1a2e';ctx.fillRect(0,0,300,300);
ctx.fillStyle='#E74C3C';ctx.fillRect(food.x*g,food.y*g,g-1,g-1);
snake.forEach((s,i)=>{ctx.fillStyle=i==0?'#6C5CE7':'#A29BFE';ctx.fillRect(s.x*g,s.y*g,g-1,g-1);});}
setInterval(()=>{update();draw();},150);
</script></body></html>
""".trimIndent()

private fun htmlDemoDashboard(): String = """
<!DOCTYPE html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<style>*{margin:0;padding:0;box-sizing:border-box}body{background:#0f0f1a;color:#fff;font-family:sans-serif;padding:16px}
.grid{display:grid;grid-template-columns:1fr 1fr;gap:12px}.card{background:#1a1a2e;border-radius:12px;padding:16px}
.card h3{font-size:14px;color:#A29BFE;margin-bottom:8px}.num{font-size:32px;font-weight:bold}
canvas{width:100%;height:80px}</style>
</head><body>
<div class="grid">
<div class="card"><h3>总用户</h3><div class="num" style="color:#6C5CE7" id="u">0</div></div>
<div class="card"><h3>活跃</h3><div class="num" style="color:#00CEC9" id="a">0</div></div>
<div class="card"><h3>收入</h3><div class="num" style="color:#2ECC71" id="r">0</div></div>
<div class="card"><h3>转化率</h3><div class="num" style="color:#F39C12" id="c">0%</div></div>
<div class="card" style="grid-column:span 2"><h3>趋势图</h3><canvas id="chart"></canvas></div>
</div>
<script>
let data=[];for(let i=0;i<20;i++)data.push(Math.random()*100);
function update(){const u=Math.floor(Math.random()*5000+1000);const a=Math.floor(u*0.3);const r=(u*0.05).toFixed(0);const c=(Math.random()*5+1).toFixed(1);
document.getElementById('u').textContent=u.toLocaleString();document.getElementById('a').textContent=a.toLocaleString();document.getElementById('r').textContent='¥'+r;document.getElementById('c').textContent=c+'%';
data.shift();data.push(Math.random()*100);
const cv=document.getElementById('chart'),ctx=cv.getContext('2d');cv.width=cv.offsetWidth;cv.height=80;
ctx.clearRect(0,0,cv.width,80);ctx.strokeStyle='#6C5CE7';ctx.lineWidth=2;ctx.beginPath();
data.forEach((v,i)=>{const x=i/(data.length-1)*cv.width;const y=80-v*0.7;if(i==0)ctx.moveTo(x,y);else ctx.lineTo(x,y);});ctx.stroke();
ctx.lineTo(cv.width,80);ctx.lineTo(0,80);ctx.fillStyle='rgba(108,92,231,0.1)';ctx.fill();}
update();setInterval(update,2000);
</script></body></html>
""".trimIndent()

private fun htmlDemoMarkdown(): String = """
<!DOCTYPE html><html><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
<style>*{margin:0;padding:0;box-sizing:border-box}body{display:flex;flex-direction:column;height:100vh;font-family:sans-serif}
.toolbar{background:#6C5CE7;color:#fff;padding:10px;font-weight:bold}textarea{flex:1;border:none;padding:12px;font-size:14px;resize:none;outline:none}
</style></head><body>
<div class="toolbar">Markdown 编辑器</div>
<textarea placeholder="# 标题&#10;## 二级标题&#10;&#10;**粗体** *斜体* &#10;&#10;- 列表项&#10;- 列表项&#10;&#10;[链接](https://example.com)&#10;&#10;`code`&#10;&#10;> 引用块" oninput="document.title='ok'"># AIHer Markdown 编辑器

输入 **Markdown** 文本，实时预览效果。

## 功能
- 标题、粗体、斜体
- 列表、引用
- 链接、代码

> 提示：在手机上也能轻松编辑</textarea>
</body></html>
""".trimIndent()