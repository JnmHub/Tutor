# 家教平台小程序 - 项目开发需求文档

**版本 V1.3**

## 1. 项目概述

### 1.1 项目简介

本项目旨在开发一个连接学生与教师的家教服务平台小程序。平台核心功能包括：教师发布可预约的课程时间段，学生根据需求筛选、查找并报名课程。平台通过后台管理员对教师资质进行审核并处理线下支付确认，最终形成一个完整的家教服务闭环。

### 1.2 核心目标

- 为学生提供一个透明、便捷的渠道，寻找并报名合适的家教课程。
- 为教师提供一个灵活的平台，发布自己的教学服务并管理课程时间。
- 为平台管理员提供一套高效的管理工具，用于审核教师、管理订单和维护平台秩序。

### 1.3 系统架构选择

根据您的要求，本系统在数据库层面采用以下设计：

- **主键（Primary Key）**: 全部使用 UUID (CHAR(36)) 作为主键，有利于未来系统向分布式架构演进，避免了ID冲突问题。
- **关联关系**: 不使用数据库物理外键 (FOREIGN KEY)。所有表之间的关联和数据一致性将由应用程序的业务逻辑层来保证。这为数据库的水平扩展和维护提供了更大的灵活性，但对后端代码的严谨性提出了更高要求。

## 2. 核心用户角色与职责

系统包含三个核心角色：

| 角色 | 主要职责 |
| --- | --- |
| 学生 (Student) | 在注册时一次性完整填写所有个人信息和成绩，之后进行登录、浏览/搜索课程、报名、评价等操作。 |
| 教师 (Teacher) | 注册登录、提交资质待审、发布/管理课程及可授课时间、查看已报名学生信息。 |
| 管理员 (Admin) | 后台登录、审核教师资质、管理所有用户和课程、确认订单收款、处理平台内容。 |

## 3. 数据库表结构

以下是系统最终确定的9个核心数据表结构。所有表名均已添加 jnm_ 前缀。



```sql
CREATE TABLE `jnm_students` (
  `id` CHAR(36) NOT NULL COMMENT '学生ID (UUID)',
  `avatar` VARCHAR(255) NOT NULL COMMENT '头像URL',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `age` TINYINT UNSIGNED NOT NULL COMMENT '年龄',
  `grade` TINYINT UNSIGNED NOT NULL COMMENT '年级 (4-9)',
  `phone` VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号',
  `address` VARCHAR(255) NOT NULL COMMENT '地址',
  `account` VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号',
  `password` VARCHAR(255) NOT NULL COMMENT '登录密码 (加密存储)',
  `education_system` ENUM('6-3', '5-4') NOT NULL COMMENT '学制 (6-3制, 5-4制)',
  `status` ENUM('active', 'banned') NOT NULL DEFAULT 'active' COMMENT '状态 (正常, 禁用)',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生信息表';

CREATE TABLE `jnm_student_scores` (
  `id` CHAR(36) NOT NULL COMMENT '成绩ID (UUID)',
  `student_id` CHAR(36) NOT NULL COMMENT '关联的学生ID',
  `subject_name` VARCHAR(50) NOT NULL COMMENT '科目名称',
  `score` DECIMAL(5, 1) NOT NULL COMMENT '科目分数',
  PRIMARY KEY (`id`),
  INDEX `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生成绩表';

CREATE TABLE `jnm_teachers` (
  `id` CHAR(36) NOT NULL COMMENT '教师ID (UUID)',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `age` TINYINT UNSIGNED NULL COMMENT '年龄',
  `id_card_photo` VARCHAR(255) NOT NULL COMMENT '身份证照片URL',
  `avatar` VARCHAR(255) NULL COMMENT '头像URL',
  `account` VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号',
  `password` VARCHAR(255) NOT NULL COMMENT '登录密码 (加密存储)',
  `status` ENUM('pending', 'approved', 'banned') NOT NULL DEFAULT 'pending' COMMENT '状态 (待审核, 正常, 禁用)',
  `teaching_subjects` VARCHAR(255) NULL COMMENT '擅长科目 (逗号分隔)',
  `teaching_experience` TEXT NULL COMMENT '教学经验描述',
  `university` VARCHAR(100) NULL COMMENT '毕业院校',
  `avg_teaching_attitude_score` DECIMAL(3, 2) NOT NULL DEFAULT 0.00 COMMENT '教学态度平均分',
  `avg_service_attitude_score` DECIMAL(3, 2) NOT NULL DEFAULT 0.00 COMMENT '服务态度平均分',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师信息表';

CREATE TABLE `jnm_courses` (
  `id` CHAR(36) NOT NULL COMMENT '课程ID (UUID)',
  `teacher_id` CHAR(36) NOT NULL COMMENT '发布课程的教师ID',
  `title` VARCHAR(100) NOT NULL COMMENT '课程标题',
  `description` TEXT NOT NULL COMMENT '课程介绍',
  `subject` VARCHAR(50) NOT NULL COMMENT '科目',
  `price` DECIMAL(10, 2) NOT NULL COMMENT '价格 (每课时)',
  `is_deleted` BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否软删除 (true为删除)',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程信息表';

CREATE TABLE `jnm_schedules` (
  `id` CHAR(36) NOT NULL COMMENT '排课ID (UUID)',
  `course_id` CHAR(36) NOT NULL COMMENT '关联的课程ID',
  `teacher_id` CHAR(36) NOT NULL COMMENT '关联的教师ID',
  `course_date` DATE NOT NULL COMMENT '上课日期',
  `start_time` TIME NOT NULL COMMENT '开始时间',
  `end_time` TIME NOT NULL COMMENT '结束时间',
  `status` ENUM('available', 'booked', 'completed') NOT NULL DEFAULT 'available' COMMENT '状态 (可选, 已预约, 已完成)',
  PRIMARY KEY (`id`),
  INDEX `idx_course_id` (`course_id`),
  INDEX `idx_teacher_id_date` (`teacher_id`, `course_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程排期表';

CREATE TABLE `jnm_enrollments` (
  `id` CHAR(36) NOT NULL COMMENT '报名ID (UUID)',
  `student_id` CHAR(36) NOT NULL COMMENT '学生ID',
  `teacher_id` CHAR(36) NOT NULL COMMENT '教师ID',
  `course_id` CHAR(36) NOT NULL COMMENT '原始课程ID',
  `schedule_id` CHAR(36) NOT NULL UNIQUE COMMENT '所选的具体排课ID',
  `copied_course_title` VARCHAR(100) NOT NULL COMMENT '报名时拷贝的课程标题',
  `copied_subject` VARCHAR(50) NOT NULL COMMENT '报名时拷贝的科目',
  `copied_price` DECIMAL(10, 2) NOT NULL COMMENT '报名时拷贝的价格',
  `status` ENUM('active', 'completed') NOT NULL DEFAULT 'active' COMMENT '学习状态 (进行中, 已完成)',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
  PRIMARY KEY (`id`),
  INDEX `idx_student_id` (`student_id`),
  INDEX `idx_teacher_id` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生报课记录表';

CREATE TABLE `jnm_reviews` (
  `id` CHAR(36) NOT NULL COMMENT '评价ID (UUID)',
  `enrollment_id` CHAR(36) NOT NULL UNIQUE COMMENT '关联的报课记录ID',
  `student_id` CHAR(36) NOT NULL COMMENT '评价的学生ID',
  `teacher_id` CHAR(36) NOT NULL COMMENT '被评价的教师ID',
  `teaching_attitude_score` TINYINT UNSIGNED NOT NULL COMMENT '教学态度分数 (1-5)',
  `service_attitude_score` TINYINT UNSIGNED NOT NULL COMMENT '服务态度分数 (1-5)',
  `comment` TEXT NULL COMMENT '文字评论内容',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  PRIMARY KEY (`id`),
  INDEX `idx_teacher_id` (`teacher_id`),
  INDEX `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教师评价表';

CREATE TABLE `jnm_orders` (
  `id` CHAR(36) NOT NULL COMMENT '订单ID (UUID)',
  `enrollment_id` CHAR(36) NOT NULL UNIQUE COMMENT '关联的报课记录ID',
  `student_id` CHAR(36) NOT NULL COMMENT '下单学生ID',
  `amount` DECIMAL(10, 2) NOT NULL COMMENT '订单金额',
  `status` ENUM('pending_confirmation', 'confirmed', 'cancelled') NOT NULL DEFAULT 'pending_confirmation' COMMENT '订单状态 (待确认, 已确认, 已取消)',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '订单更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_student_id` (`student_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

CREATE TABLE `jnm_admins` (
  `id` CHAR(36) NOT NULL COMMENT '管理员ID (UUID)',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `account` VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号',
  `password` VARCHAR(255) NOT NULL COMMENT '登录密码 (加密存储)',
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员信息表';
```
## 4. 核心业务流程 (User Stories)
### 4.1 用户注册与登录流程 学生注册流程 (事务性操作)：
【前端逻辑】 :

- 学生在注册页面需要填写一个完整的表单，该表单包含所有 jnm_students 表中的必填项（头像、姓名、年龄、年级、手机、地址、账号、密码、学制）以及根据所选年级动态生成的各科成绩输入框。
- 前端对所有字段进行非空校验，所有信息填写完毕后才能点击"注册"按钮。
【后端逻辑】 :

1. 开启一个数据库事务。
2. 首先，将个人信息插入到 jnm_students 表。
3. 获取新生成的学生ID。
4. 然后，将表单中提交的各科成绩（一个包含多个科目和分数的数组）遍历，逐条插入到 jnm_student_scores 表，并关联上一步的学生ID。
5. 如果所有插入操作都成功，则提交事务，注册成功。
6. 如果在任何一步发生错误（例如手机号或账号已存在），则回滚整个事务，保证数据一致性，并向前端返回注册失败的错误信息。 教师注册流程：
1. 教师填写账号、密码、姓名、并上传身份证照片。
2. 提交后，系统创建一条 jnm_teachers 记录，status 为 pending (待审核)。
3. 教师此时无法登录或发布课程。 用户登录：
1. 用户（学生或教师）使用账号和密码登录。
2. 后端仅需验证账号密码是否正确。正确则发放登录凭证（Token），用户进入主界面。不再需要检查信息是否完整。 管理员审核教师：
1. 管理员登录后台管理系统，进入"教师审核"页面，看到所有 status 为 pending 的教师列表。
2. 管理员点击查看教师详情，核对身份证照片等信息。
3. 审核通过：将该教师记录的 status 修改为 approved。该教师账号即可正常登录。
4. 审核驳回：将 status 修改为 banned 或直接删除记录，并可附加驳回原因。
### 4.2 教师发布与管理课程流程 发布新课程：
1. 已审核通过的教师登录后，进入"我的课程" -> "发布课程"。
2. 填写课程信息（标题、描述、科目、价格），创建一条 jnm_courses 记录。 添加授课时间：
1. 在课程下，点击"添加时间"按钮。
2. 选择日期 (course_date)、输入开始时间 (start_time) 和结束时间 (end_time)。
3. 【后端逻辑】 ：在提交时，系统必须检查该教师在 jnm_schedules 表中，于 course_date 当天是否已存在时间重叠的安排。如果重叠，则提示错误；否则，创建一条 jnm_schedules 记录，status 为 available。
4. 教师可以为一个课程添加多个不冲突的时间段。 管理课程：
1. 教师可以对无人报名的课程进行编辑或软删除 (is_deleted = true)。
2. 对于已有学生报名的课程，为保证订单信息有效，不允许删除。
### 4.3 学生发现与报名课程流程 浏览与筛选：
1. 学生打开小程序首页，默认看到所有未被软删除 (is_deleted = false) 且有关联的 available 排课的课程列表。
2. 提供搜索框（按课程标题/科目/教师姓名）和排序功能（按价格、按教师评分）。 查看详情：
1. 点击任意课程，进入课程详情页。
2. 页面展示 jnm_courses 表的信息、关联的 jnm_teachers 表信息（头像、姓名、教龄、评分等）以及该教师收到的所有 jnm_reviews 评价列表。 选择时间并报名：
1. 详情页下方展示该课程所有关联的 jnm_schedules 列表。
2. status 为 available 的时间段显示为绿色可选状态。
3. status 为 booked 或 completed 的时间段显示为灰色不可选状态。
4. 学生点击一个绿色时间段，点击"立即报名"按钮。 生成订单：
【后端逻辑】 ：这是一个事务性操作。

1. 锁定该条 jnm_schedules 记录，将其 status 从 available 更新为 booked。
2. 在 jnm_enrollments 表中创建一条新的报课记录，拷贝课程标题、价格等信息，并关联 student_id, teacher_id, course_id, schedule_id。
3. 在 jnm_orders 表中创建一条新订单，status 为 pending_confirmation。
【前端逻辑】 ：操作成功后，跳转到支付提示页面，展示客服二维码和订单信息，提示学生联系客服完成线下支付。

### 4.4 支付确认与课后评价流程 管理员确认支付：
1. 学生线下支付后，管理员在后台找到对应的 jnm_orders 记录（可按学生手机号或订单创建时间查询）。
2. 核实无误后，将该订单 status 修改为 confirmed。至此，报名流程完全成功。 课程完成：
当 jnm_schedules 中记录的 course_date 和 end_time 过去后，系统可以将 jnm_schedules 的 status 自动更新为 completed，同时将关联的 jnm_enrollments 记录的 status 更新为 completed。（此步骤也可由管理员或教师手动完成）
 学生评价：
1. 在学生的"我的课程"页面，对于 status 为 completed 的 jnm_enrollments 记录，会显示一个"去评价"按钮。
2. 学生点击按钮，进入评价页面，填写对教师的"教学态度"和"服务态度"分数，以及文字评论。
3. 提交后，在 jnm_reviews 表中创建一条新记录。 更新教师评分：
【后端逻辑】 ：在 jnm_reviews 表中插入新数据后，触发一个计算任务。

1. 该任务会查询该教师所有 jnm_reviews 记录，重新计算平均的 teaching_attitude_score 和 service_attitude_score。
2. 将计算出的新平均分更新回 jnm_teachers 表对应的教师记录中，用于前端展示。
## 5. 技术栈选型

根据您的决定，项目将采用以下技术栈进行开发。

| 层面 | 技术/框架 | 备注 |
|------|-----------|------|
| 小程序端 (Frontend) | uni-app (Vue 3 + TypeScript) | 使用 Vue 3 的 Composition API 组织代码，并利用 TypeScript 提供类型安全，提升代码质量和可维护性。可使用 Pinia 进行状态管理。 |
| 后台服务 (Backend) | Java (Spring Boot) | 采用 Spring Boot 2.x 或 3.x 版本。建议使用 Maven 或 Gradle 进行项目构建和依赖管理。数据持久层可选用 MyBatis-Plus 或 Spring Data JPA 来简化数据库操作。 |
| 数据库 (Database) | MySQL 8.0+ | 项目指定的数据库。 |
| 服务器 (Server) | 阿里云 / 腾讯云 ECS | 选择国内主流云服务商，按需配置服务器规格。 |
| Web 服务器 | Nginx | 用于反向代理、负载均衡和静态资源处理。 |
| 部署工具 | Docker | 强烈建议使用 Docker 进行容器化部署，可以封装整个后端应用及环境，实现一次构建、处处运行，极大简化部署和运维流程。 |

## 6. 下一步行动
1. 环境搭建 ：根据技术栈选型，搭建前后端开发环境。
2. 原型设计 (UI/UX) ：根据本文档的业务流程，设计小程序所有页面的UI视觉稿和交互原型。
3. API接口定义 ：基于业务流程，设计详细的RESTful API接口，明确请求/响应格式。
4. 并行开发 ：前端和后端可以根据定义好的API接口并行进行开发工作。
5. 测试与部署 ：完成开发后进行单元测试、集成测试，最后部署上线。
这份文档为您定义了项目的"做什么"和"怎么做"，是整个开发过程的基石。请您再次审阅，如果确认无误，就可以基于此文档开始后续的技术开发工作了。