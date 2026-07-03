# DevPilot AI Learning Roadmap

这份文档用于记录 DevPilot AI 项目的学习路线、阶段目标、核心知识点和阶段复盘。后续每完成一个功能，都可以把设计原因、实现步骤、注意点和面试表达整理到这里。

## 学习方式

本项目采用“先理解，再设计，再实现，最后复盘”的节奏。

1. 先讲为什么：理解这个功能解决什么问题，为什么要这样拆分。
2. 再讲怎么设计：明确前端、后端、数据库、AI 模块分别负责什么。
3. 然后小步实现：一次只做一个清晰的小目标，保持代码可运行。
4. 最后做复盘：总结知识点、常见坑、优化方向和面试表达。

## 项目定位

DevPilot AI 是一个 AI 知识库与项目助理系统，核心目标是练习 Java 后端、Vue 3 前端和 AI 应用工程能力。

用户可以上传项目文档、需求文档、接口文档或代码说明，系统会解析文档、切分文本、生成向量、保存到向量数据库。之后用户可以围绕知识库提问，系统通过 RAG 检索相关内容并生成回答。

## 技术栈

后端：

- Java 21
- Spring Boot 3
- Spring Web
- Spring Validation
- Spring Actuator
- 后续引入 Spring Data JPA、Spring Security、Spring AI
- PostgreSQL + pgvector
- Redis

前端：

- Vue 3
- TypeScript
- Vite
- Vue Router
- Pinia
- Axios
- 后续引入 Markdown 渲染、代码高亮、SSE 流式输出

工程化：

- Git + GitHub
- GitHub Actions
- Docker Compose

## 阶段 1：项目启动与工程结构

目标：把项目完整跑起来，并理解前后端为什么这样组织。

要完成的功能：

- 后端启动成功
- 前端启动成功
- 前端通过 `/api` 调用后端
- 理解 monorepo 目录结构
- 理解 GitHub 仓库与本地仓库关系

重点知识：

- monorepo 是什么
- Spring Boot 项目基本结构
- Vue 3 + Vite 项目基本结构
- 为什么后端常用 `8080`
- 为什么 Vite 默认常用 `5173`
- 为什么前端开发时需要 proxy
- 什么是 CORS

当前启动命令：

```bash
cd /Users/zhuzhendong/Documents/前端转全栈/devpilot-ai/backend
./mvnw spring-boot:run
```

另开一个 zsh 窗口：

```bash
cd /Users/zhuzhendong/Documents/前端转全栈/devpilot-ai/frontend
npm run dev
```

浏览器访问：

```text
http://localhost:5173
```

健康检查接口：

```text
http://localhost:8080/api/health
```

## 阶段 2：后端基础 API

目标：写出规范、可维护的 Java 后端接口。

要完成的功能：

- 知识库 CRUD
- 文档记录 CRUD
- 统一响应结构
- 统一异常处理
- 参数校验
- 数据库连接

重点知识：

- Controller / Service / Repository 分层
- DTO 和 Entity 的区别
- RESTful API 设计
- Bean Validation
- 统一异常处理
- 配置文件与环境变量
- 数据库迁移工具的作用

注意点：

- Controller 不写复杂业务逻辑。
- Service 负责业务流程编排。
- Repository 负责数据访问。
- 不直接把 Entity 暴露给前端，优先使用 DTO。

## 阶段 3：前端基础页面

目标：用 Vue 3 做出可维护的业务页面。

要完成的功能：

- 知识库列表页
- 知识库详情页
- 文档列表
- 文档上传入口
- 问答页面基础交互

重点知识：

- Composition API
- 单文件组件 SFC
- Pinia 状态管理
- Vue Router
- Axios 封装
- loading、empty、error 状态
- 组件拆分边界

注意点：

- 页面组件负责组织业务视图。
- 通用组件负责复用 UI。
- API 请求统一放在 `src/api`。
- 跨页面共享状态再放入 Pinia，不要什么都放 store。

## 阶段 4：知识库核心功能

目标：实现真正的知识库数据流。

要完成的功能：

- 文档上传
- 文档类型校验
- 文档解析
- 文本切分 chunk
- chunk 元数据保存
- 异步处理状态

重点知识：

- 文件上传协议
- MultipartFile
- PDF / Markdown / TXT 解析
- 文本切分策略
- chunk size 和 overlap
- 同步处理和异步处理的取舍
- 任务状态设计

注意点：

- 不要一开始就追求支持所有文件类型。
- 先支持 Markdown / TXT，再扩展 PDF。
- 文档处理可能耗时，后续应改成异步任务。
- chunk 必须保留来源信息，否则回答无法给引用。

## 阶段 5：AI / RAG 核心

目标：实现“基于文档内容回答问题”。

要完成的功能：

- 文本 embedding
- 向量入库
- 相似度检索
- Prompt 上下文拼接
- AI 回答
- 返回引用来源

重点知识：

- 什么是 embedding
- 什么是向量数据库
- 什么是相似度搜索
- 什么是 RAG
- 为什么不能把全部文档直接塞给模型
- Prompt 模板如何设计
- hallucination 是什么

注意点：

- 检索结果不是越多越好。
- Prompt 里要要求模型基于上下文回答。
- 回答要带 sources，方便用户判断可信度。
- embedding 模型和 chat 模型可以不同。

## 阶段 6：聊天体验

目标：做出接近真实 AI 产品的交互体验。

要完成的功能：

- 多轮会话
- 聊天历史
- SSE 流式输出
- Markdown 渲染
- 代码高亮
- 回答重新生成
- 知识库切换

重点知识：

- SSE 是什么
- SSE 和 WebSocket 的区别
- 前端如何消费流式响应
- 多轮上下文如何保存
- Markdown 安全渲染

注意点：

- 流式输出能明显提升用户体验。
- 聊天历史和知识库上下文是两件事。
- 不要无限制把历史消息塞进模型。

## 阶段 7：登录、权限与部署

目标：把项目打磨成作品集级别。

要完成的功能：

- 用户注册登录
- JWT 认证
- Spring Security
- 知识库权限控制
- 用量统计
- Docker Compose 一键启动
- GitHub Actions CI
- 部署说明

重点知识：

- 认证和授权的区别
- JWT 的结构
- Spring Security 过滤器链
- 多用户数据隔离
- Docker 网络
- CI 的价值

注意点：

- 权限逻辑必须在后端校验，不能只靠前端隐藏按钮。
- 密码不能明文保存。
- 生产环境不要把 API key 写进代码或提交到 GitHub。

## 阶段复盘模板

每完成一个阶段，用下面模板复盘。

```markdown
## 阶段 X 复盘

完成时间：

完成内容：

我学到了：

关键代码：

踩过的坑：

还能优化：

面试可以这样讲：
```

## 当前学习进度

- [x] 创建项目骨架
- [x] 推送到 GitHub
- [x] 跑通后端服务
- [x] 跑通前端服务
- [x] 理解前后端请求链路
- [x] 引入后端 Service 层
- [x] 引入后端 Repository 层

## 学习笔记 01：前后端请求链路

本节目标：理解用户在页面上点击一次提问按钮后，数据如何从 Vue 页面流向 Spring Boot 后端，再回到页面。

### 当前链路

```text
WorkspaceView.vue
  -> Pinia store: useKnowledgeStore
  -> API function: askKnowledgeBase
  -> Axios instance: http
  -> Vite dev server proxy: /api -> http://localhost:8080
  -> Spring Controller: KnowledgeController
  -> JSON response
  -> Pinia state update
  -> Vue template re-render
```

### 前端页面层

入口文件：

```text
frontend/src/views/WorkspaceView.vue
```

页面层负责：

- 展示输入框、按钮、回答区域。
- 接收用户输入的问题。
- 响应按钮点击事件。
- 从 store 读取 loading、answer、sources 等状态。

这里有一个核心方法：

```ts
function submitQuestion() {
  const trimmed = question.value.trim()
  if (!trimmed) return
  void store.ask(trimmed)
}
```

注意点：

- 页面不直接调用 axios，而是调用 store。
- 页面不关心接口地址，也不关心后端怎么实现。
- `trim()` 用于避免用户提交空白问题。
- `void store.ask(trimmed)` 表示触发异步动作，但当前函数不等待它返回。

### 前端状态层

入口文件：

```text
frontend/src/stores/knowledge.ts
```

Pinia store 负责管理知识库相关状态：

- `knowledgeBases`：知识库列表。
- `currentKnowledgeBaseId`：当前选中的知识库。
- `loading`：接口请求中状态。
- `latestAnswer`：最近一次回答。

提问时：

```ts
async function ask(question: string) {
  loading.value = true
  try {
    latestAnswer.value = await askKnowledgeBase(currentKnowledgeBaseId.value, question)
  } finally {
    loading.value = false
  }
}
```

注意点：

- `loading` 放在 store 中，页面就可以根据它禁用按钮。
- `try/finally` 确保请求成功或失败后都会关闭 loading。
- store 不直接写请求细节，而是调用 `src/api` 里的函数。

### 前端 API 层

入口文件：

```text
frontend/src/api/knowledge.ts
```

API 层负责把业务动作转换成 HTTP 请求：

```ts
export async function askKnowledgeBase(knowledgeBaseId: string, question: string) {
  const { data } = await http.post<AskKnowledgeResponse>(
    `/knowledge-bases/${knowledgeBaseId}/ask`,
    { question },
  )
  return data
}
```

注意点：

- API 函数名应该表达业务含义，而不是叫 `postData`。
- TypeScript interface 描述了前后端约定的数据结构。
- 这里的路径是 `/knowledge-bases/...`，不是完整的 `http://localhost:8080/...`，因为 `http` 已经配置了 baseURL。

### Axios 封装层

入口文件：

```text
frontend/src/api/http.ts
```

当前配置：

```ts
export const http = axios.create({
  baseURL: '/api',
  timeout: 30000,
})
```

这意味着：

- `http.get('/knowledge-bases')` 实际请求的是 `/api/knowledge-bases`。
- 超时时间是 30 秒，避免请求无限等待。

后续可以在这里统一加：

- JWT token
- 请求日志
- 错误处理
- 401 跳转登录

### Vite 代理层

入口文件：

```text
frontend/vite.config.ts
```

当前配置：

```ts
server: {
  port: 5173,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
    },
  },
}
```

开发环境中的请求流向：

```text
浏览器 -> http://localhost:5173/api/knowledge-bases
Vite  -> http://localhost:8080/api/knowledge-bases
```

为什么需要代理：

- 前端开发服务器和后端服务器端口不同。
- 浏览器会把不同端口视为不同源。
- proxy 让前端代码像调用同源接口一样调用后端。
- 后续部署到生产环境时，通常由 Nginx 或网关承担类似职责。

### 后端 Controller 层

入口文件：

```text
backend/src/main/java/com/devpilot/ai/knowledge/KnowledgeController.java
```

当前 Controller：

```java
@RestController
@RequestMapping("/api/knowledge-bases")
public class KnowledgeController {
    @GetMapping
    public List<KnowledgeBaseSummary> listKnowledgeBases() {
        return List.of(
                new KnowledgeBaseSummary("demo", "Demo Knowledge Base", 0)
        );
    }
}
```

`@RestController` 的作用：

- 标记这是一个处理 HTTP 请求的类。
- 方法返回值会自动序列化成 JSON。

`@RequestMapping("/api/knowledge-bases")` 的作用：

- 给这个 Controller 下的所有接口统一加路径前缀。

`@GetMapping` 的作用：

- 表示这个方法处理 HTTP GET 请求。

当前后端还没有数据库，所以先返回写死的 demo 数据。这是合理的工程步骤，因为我们先验证前后端链路，再引入数据库复杂度。

### POST 提问接口

当前接口：

```java
@PostMapping("/{knowledgeBaseId}/ask")
public AskKnowledgeResponse ask(
        @PathVariable String knowledgeBaseId,
        @Valid @RequestBody AskKnowledgeRequest request
) {
    return new AskKnowledgeResponse(
            "This is a placeholder answer for knowledge base `%s`: %s"
                    .formatted(knowledgeBaseId, request.question()),
            List.of(new SourceReference("README.md", "Project overview", 0.92))
    );
}
```

这里有三个重要概念：

- `@PathVariable`：从 URL 路径中取变量，比如 `demo`。
- `@RequestBody`：从请求体 JSON 中读取数据。
- `@Valid`：触发参数校验。

请求示例：

```http
POST /api/knowledge-bases/demo/ask
Content-Type: application/json

{
  "question": "请总结这个知识库"
}
```

响应示例：

```json
{
  "answer": "This is a placeholder answer for knowledge base `demo`: 请总结这个知识库",
  "sources": [
    {
      "documentName": "README.md",
      "snippet": "Project overview",
      "score": 0.92
    }
  ]
}
```

### 为什么现在先写假数据

这不是偷懒，而是工程实践中的“垂直切片”。

垂直切片的意思是：先让一个最小功能从页面到后端完整跑通，再逐层替换内部实现。

当前切片：

```text
页面输入 -> 接口请求 -> 后端响应 -> 页面展示
```

后续我们会逐步替换：

- 写死知识库 -> 数据库知识库
- 写死回答 -> RAG 检索回答
- 写死来源 -> 真实文档 chunk 来源
- 普通响应 -> SSE 流式响应

好处：

- 每一步都能运行。
- 出问题时容易定位。
- 学习曲线更平滑。
- 更符合真实团队开发节奏。

### 本节你应该掌握

- 前端页面、store、api 三层分别负责什么。
- 为什么 axios 要封装。
- 为什么开发环境需要 Vite proxy。
- Spring Boot Controller 如何接收 GET / POST 请求。
- `@PathVariable`、`@RequestBody`、`@Valid` 的基本作用。
- 为什么先用 placeholder 数据跑通链路。

## 学习笔记 02：为什么要引入 Service 层

本节目标：把后端从“Controller 直接写业务逻辑”调整为“Controller 接收请求，Service 处理业务”。

### 改动前的问题

最初的 `KnowledgeController` 直接返回 demo 数据：

```java
@GetMapping
public List<KnowledgeBaseSummary> listKnowledgeBases() {
    return List.of(
            new KnowledgeBaseSummary("demo", "Demo Knowledge Base", 0)
    );
}
```

这在项目刚搭起来时可以接受，因为它帮助我们快速跑通前后端链路。但如果继续把所有逻辑都写在 Controller 里，后面会出现几个问题：

- Controller 会越来越胖。
- 数据库查询、文档解析、AI 调用、权限判断混在一起。
- 单元测试很难写。
- 同一段业务逻辑无法被多个接口复用。
- 前端接口层和后端业务层的边界会变模糊。

### Controller 应该负责什么

Controller 是 HTTP 入口层，主要职责是处理 Web 相关的事情：

- 定义 URL。
- 区分 GET / POST / PUT / DELETE。
- 读取路径参数、查询参数、请求体。
- 触发参数校验。
- 调用业务层。
- 返回响应对象。

Controller 不适合放复杂业务规则。

### Service 应该负责什么

Service 是业务层，主要职责是表达业务流程：

- 创建知识库。
- 查询知识库。
- 上传文档后的处理流程。
- 调用文档解析器。
- 调用向量检索。
- 拼接 Prompt。
- 调用 AI 模型。
- 返回业务结果。

后续真实的提问流程会长这样：

```text
KnowledgeController
  -> KnowledgeService
  -> DocumentChunkRepository
  -> VectorStore
  -> ChatModel
  -> AskKnowledgeResponse
```

所以现在先引入 Service 层，是为了给后续复杂业务留出位置。

### 本次代码结构

当前后端知识库模块：

```text
backend/src/main/java/com/devpilot/ai/knowledge/
  KnowledgeController.java
  KnowledgeService.java
  AskKnowledgeRequest.java
  AskKnowledgeResponse.java
  KnowledgeBaseSummary.java
  SourceReference.java
```

`KnowledgeController`：

```java
@RestController
@RequestMapping("/api/knowledge-bases")
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    public KnowledgeController(KnowledgeService knowledgeService) {
        this.knowledgeService = knowledgeService;
    }
}
```

`KnowledgeService`：

```java
@Service
public class KnowledgeService {
    public List<KnowledgeBaseSummary> listKnowledgeBases() {
        return List.of(
                new KnowledgeBaseSummary("demo", "Demo Knowledge Base", 0)
        );
    }
}
```

### Spring 是如何把 Service 传给 Controller 的

`KnowledgeService` 上有一个注解：

```java
@Service
```

它告诉 Spring：这个类是一个业务组件，请帮我创建对象并管理它。

Controller 构造函数里声明需要 `KnowledgeService`：

```java
public KnowledgeController(KnowledgeService knowledgeService) {
    this.knowledgeService = knowledgeService;
}
```

Spring 启动时会自动找到 `KnowledgeService`，创建实例，然后传给 `KnowledgeController`。这就是依赖注入。

### 为什么推荐构造函数注入

本项目使用构造函数注入，而不是字段注入：

```java
private final KnowledgeService knowledgeService;

public KnowledgeController(KnowledgeService knowledgeService) {
    this.knowledgeService = knowledgeService;
}
```

好处：

- 依赖关系一眼能看出来。
- 字段可以声明为 `final`，对象创建后不可变。
- 更容易写测试。
- 如果缺少依赖，应用启动时就会失败。

### 本节你应该掌握

- Controller 是 HTTP 入口层。
- Service 是业务层。
- `@Service` 表示让 Spring 管理业务组件。
- 构造函数注入是 Spring 项目里推荐的依赖注入方式。
- 这次改动不改变接口行为，只调整代码职责边界。

## 学习笔记 03：为什么要引入 Repository 层

本节目标：把“数据从哪里来”这件事从 Service 中拆出去，形成 `Controller -> Service -> Repository` 三层结构。

### 三层分别负责什么

当前后端知识库模块的调用链：

```text
KnowledgeController
  -> KnowledgeService
  -> KnowledgeRepository
  -> InMemoryKnowledgeRepository
```

三层职责：

- Controller：负责 HTTP 请求和响应。
- Service：负责业务流程和业务规则。
- Repository：负责数据访问。

这三个层次不要互相抢活。

### 为什么 Service 不应该直接写数据

改动前，Service 中直接写了 demo 数据：

```java
return List.of(
        new KnowledgeBaseSummary("demo", "Demo Knowledge Base", 0)
);
```

这会让 Service 同时承担两件事：

- 业务逻辑。
- 数据来源。

短期看没问题，但当数据来源从内存变成数据库、缓存、远程服务后，Service 会越来越难维护。

### Repository 的作用

Repository 层负责提供数据访问方法：

```java
public interface KnowledgeRepository {

    List<KnowledgeBase> findAll();

    Optional<KnowledgeBase> findById(String id);
}
```

注意这里定义的是接口，而不是直接把实现写死在 Service 里。

这样做的好处：

- Service 只依赖抽象，不关心数据来自哪里。
- 以后可以把内存实现替换成数据库实现。
- 更容易写单元测试。
- 数据访问逻辑集中管理。

### 为什么现在用 InMemoryRepository

当前实现：

```java
@Repository
public class InMemoryKnowledgeRepository implements KnowledgeRepository {

    private final Map<String, KnowledgeBase> knowledgeBases = Map.of(
            "demo", new KnowledgeBase("demo", "Demo Knowledge Base", 0)
    );
}
```

它的数据存在内存里，应用重启后不会持久化。

为什么还要这样做：

- 它比直接写在 Service 里更接近真实分层。
- 还不需要引入数据库复杂度。
- 可以保持接口行为不变。
- 后续替换成 JPA Repository 时，Service 改动会很小。

这叫“逐步演进”，不是一步到位。

### Entity / Domain / DTO 的区别

本次新增了：

```text
KnowledgeBase.java
KnowledgeBaseSummary.java
```

它们看起来字段一样，但角色不同。

`KnowledgeBase` 是后端内部的业务对象，表示知识库本身：

```java
public record KnowledgeBase(
        String id,
        String name,
        long documentCount
) {
}
```

`KnowledgeBaseSummary` 是返回给前端的 DTO：

```java
public record KnowledgeBaseSummary(
        String id,
        String name,
        long documentCount
) {
}
```

为什么不直接把内部对象返回给前端：

- 后端内部字段以后可能比前端需要的更多。
- 数据库字段可能包含敏感信息。
- API 返回结构应该稳定，不要轻易被内部模型影响。
- DTO 可以根据页面需要定制。

现在字段一样只是因为项目还很小，后面差异会越来越明显。

### Optional 的作用

Repository 中的查询方法：

```java
Optional<KnowledgeBase> findById(String id);
```

`Optional` 表示：这个结果可能存在，也可能不存在。

Service 中这样处理：

```java
KnowledgeBase knowledgeBase = knowledgeRepository.findById(knowledgeBaseId)
        .orElseThrow(() -> new KnowledgeBaseNotFoundException(knowledgeBaseId));
```

这比返回 `null` 更清晰，因为它强迫调用方处理“找不到”的情况。

### 为什么要有业务异常

新增异常：

```java
public class KnowledgeBaseNotFoundException extends RuntimeException {
    public KnowledgeBaseNotFoundException(String knowledgeBaseId) {
        super("Knowledge base not found: " + knowledgeBaseId);
    }
}
```

它表达的是一个业务问题：用户请求的知识库不存在。

不要让这种情况默默变成空结果，也不要让它变成模糊的服务器错误。

### 为什么要统一异常处理

新增：

```text
common/GlobalExceptionHandler.java
common/ApiErrorResponse.java
```

当 `KnowledgeBaseNotFoundException` 被抛出时，后端返回：

```http
404 Not Found
```

响应体类似：

```json
{
  "code": "KNOWLEDGE_BASE_NOT_FOUND",
  "message": "Knowledge base not found: xxx",
  "timestamp": "2026-07-03T09:00:00Z"
}
```

这样前端就能根据稳定的 `code` 做错误提示，而不是猜测后端错误文本。

### 本节你应该掌握

- Repository 是数据访问层。
- Service 不应该关心数据具体存在哪里。
- 先用内存 Repository，是为了逐步演进到数据库。
- DTO 和内部业务对象即使字段一样，也有不同职责。
- `Optional` 可以明确表达“可能没有结果”。
- 业务异常应该被映射成合适的 HTTP 状态码。
