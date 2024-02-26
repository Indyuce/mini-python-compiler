	.text
	.globl main
main:
	movq $40, %rdi
	call __malloc__
	movq %rax, %r15
	movq $128, %rdi
	call __malloc__
	movq $__none__add__, %rdi
	movq %rdi, 0(%rax)
	movq $__none__sub__, %rdi
	movq %rdi, 8(%rax)
	movq $__none__mul__, %rdi
	movq %rdi, 16(%rax)
	movq $__none__div__, %rdi
	movq %rdi, 24(%rax)
	movq $__none__mod__, %rdi
	movq %rdi, 32(%rax)
	movq $__none__eq__, %rdi
	movq %rdi, 40(%rax)
	movq $__none__neq__, %rdi
	movq %rdi, 48(%rax)
	movq $__none__lt__, %rdi
	movq %rdi, 56(%rax)
	movq $__none__le__, %rdi
	movq %rdi, 64(%rax)
	movq $__none__gt__, %rdi
	movq %rdi, 72(%rax)
	movq $__none__ge__, %rdi
	movq %rdi, 80(%rax)
	movq $__none__neg__, %rdi
	movq %rdi, 88(%rax)
	movq $__none__not__, %rdi
	movq %rdi, 96(%rax)
	movq $__none__int__, %rdi
	movq %rdi, 104(%rax)
	movq $__none__bool__, %rdi
	movq %rdi, 112(%rax)
	movq $__none__print__, %rdi
	movq %rdi, 120(%rax)
	movq %rax, 0(%r15)
	movq $128, %rdi
	call __malloc__
	movq $__bool__add__, %rdi
	movq %rdi, 0(%rax)
	movq $__bool__sub__, %rdi
	movq %rdi, 8(%rax)
	movq $__bool__mul__, %rdi
	movq %rdi, 16(%rax)
	movq $__bool__div__, %rdi
	movq %rdi, 24(%rax)
	movq $__bool__mod__, %rdi
	movq %rdi, 32(%rax)
	movq $__int__eq__, %rdi
	movq %rdi, 40(%rax)
	movq $__int__neq__, %rdi
	movq %rdi, 48(%rax)
	movq $__int__lt__, %rdi
	movq %rdi, 56(%rax)
	movq $__int__le__, %rdi
	movq %rdi, 64(%rax)
	movq $__int__gt__, %rdi
	movq %rdi, 72(%rax)
	movq $__int__ge__, %rdi
	movq %rdi, 80(%rax)
	movq $__bool__neg__, %rdi
	movq %rdi, 88(%rax)
	movq $__bool__not__, %rdi
	movq %rdi, 96(%rax)
	movq $__bool__int__, %rdi
	movq %rdi, 104(%rax)
	movq $__bool__bool__, %rdi
	movq %rdi, 112(%rax)
	movq $__bool__print__, %rdi
	movq %rdi, 120(%rax)
	movq %rax, 8(%r15)
	movq $128, %rdi
	call __malloc__
	movq $__int__add__, %rdi
	movq %rdi, 0(%rax)
	movq $__int__sub__, %rdi
	movq %rdi, 8(%rax)
	movq $__int__mul__, %rdi
	movq %rdi, 16(%rax)
	movq $__int__div__, %rdi
	movq %rdi, 24(%rax)
	movq $__int__mod__, %rdi
	movq %rdi, 32(%rax)
	movq $__int__eq__, %rdi
	movq %rdi, 40(%rax)
	movq $__int__neq__, %rdi
	movq %rdi, 48(%rax)
	movq $__int__lt__, %rdi
	movq %rdi, 56(%rax)
	movq $__int__le__, %rdi
	movq %rdi, 64(%rax)
	movq $__int__gt__, %rdi
	movq %rdi, 72(%rax)
	movq $__int__ge__, %rdi
	movq %rdi, 80(%rax)
	movq $__int__neg__, %rdi
	movq %rdi, 88(%rax)
	movq $__bool__not__, %rdi
	movq %rdi, 96(%rax)
	movq $__int__int__, %rdi
	movq %rdi, 104(%rax)
	movq $__int__bool__, %rdi
	movq %rdi, 112(%rax)
	movq $__int__print__, %rdi
	movq %rdi, 120(%rax)
	movq %rax, 16(%r15)
	movq $128, %rdi
	call __malloc__
	movq $__string__add__, %rdi
	movq %rdi, 0(%rax)
	movq $__string__sub__, %rdi
	movq %rdi, 8(%rax)
	movq $__string__mul__, %rdi
	movq %rdi, 16(%rax)
	movq $__string__div__, %rdi
	movq %rdi, 24(%rax)
	movq $__string__mod__, %rdi
	movq %rdi, 32(%rax)
	movq $__string__eq__, %rdi
	movq %rdi, 40(%rax)
	movq $__string__neq__, %rdi
	movq %rdi, 48(%rax)
	movq $__string__lt__, %rdi
	movq %rdi, 56(%rax)
	movq $__string__le__, %rdi
	movq %rdi, 64(%rax)
	movq $__string__gt__, %rdi
	movq %rdi, 72(%rax)
	movq $__string__ge__, %rdi
	movq %rdi, 80(%rax)
	movq $__string__neg__, %rdi
	movq %rdi, 88(%rax)
	movq $__bool__not__, %rdi
	movq %rdi, 96(%rax)
	movq $__string__int__, %rdi
	movq %rdi, 104(%rax)
	movq $__int__bool__, %rdi
	movq %rdi, 112(%rax)
	movq $__string__print__, %rdi
	movq %rdi, 120(%rax)
	movq %rax, 24(%r15)
	movq $128, %rdi
	call __malloc__
	movq $__list__add__, %rdi
	movq %rdi, 0(%rax)
	movq $__list__sub__, %rdi
	movq %rdi, 8(%rax)
	movq $__list__mul__, %rdi
	movq %rdi, 16(%rax)
	movq $__list__div__, %rdi
	movq %rdi, 24(%rax)
	movq $__list__mod__, %rdi
	movq %rdi, 32(%rax)
	movq $__list__eq__, %rdi
	movq %rdi, 40(%rax)
	movq $__list__neq__, %rdi
	movq %rdi, 48(%rax)
	movq $__list__lt__, %rdi
	movq %rdi, 56(%rax)
	movq $__list__le__, %rdi
	movq %rdi, 64(%rax)
	movq $__list__gt__, %rdi
	movq %rdi, 72(%rax)
	movq $__list__ge__, %rdi
	movq %rdi, 80(%rax)
	movq $__list__neg__, %rdi
	movq %rdi, 88(%rax)
	movq $__bool__not__, %rdi
	movq %rdi, 96(%rax)
	movq $__list__int__, %rdi
	movq %rdi, 104(%rax)
	movq $__int__bool__, %rdi
	movq %rdi, 112(%rax)
	movq $__list__print__, %rdi
	movq %rdi, 120(%rax)
	movq %rax, 32(%r15)
	jmp __main__
__printf__:
	pushq %rbp
	movq %rsp, %rbp
	andq $-16, %rsp
	xorq %rax, %rax
	call printf
	movq %rbp, %rsp
	popq %rbp
	ret
__comp__length__list__:
	movq 8(%rsi), %rcx
	cmpq %rcx, 8(%rdi)
	js __min__2__
	movq %rsi, %rax
	movq %rdi, %rcx
	movq $__bool__False, %rdx
	ret
__min__2__:
	movq %rdi, %rax
	movq %rsi, %rcx
	movq $__bool__True, %rdx
	ret
__strcmp__:
	pushq %rbp
	movq %rsp, %rbp
	andq $-16, %rsp
	call strcmp
	movq %rbp, %rsp
	popq %rbp
	ret
__malloc__:
	pushq %rbp
	movq %rsp, %rbp
	andq $-16, %rsp
	call malloc
	movq %rbp, %rsp
	popq %rbp
	ret
__strcpy__:
	pushq %rbp
	movq %rsp, %rbp
	andq $-16, %rsp
	call strcpy
	movq %rbp, %rsp
	popq %rbp
	ret
__strcat__:
	pushq %rbp
	movq %rsp, %rbp
	andq $-16, %rsp
	call strcat
	movq %rbp, %rsp
	popq %rbp
	ret
__err__:
	movq $60, %rax
	movq $1, %rdi
	syscall
__len__:
	movq 0(%rdi), %r10
	cmpq $3, %r10
	je t_0
	cmpq $4, %r10
	je t_0
	jmp __err__
t_0:
	pushq %rdi
	movq $16, %rdi
	call __malloc__
	movq $2, 0(%rax)
	popq %rdi
	movq 8(%rdi), %r10
	movq %r10, 8(%rax)
	ret
__none__add__:
	jmp __err__
__none__sub__:
	jmp __err__
__none__mul__:
	jmp __err__
__none__div__:
	jmp __err__
__none__mod__:
	jmp __err__
__none__eq__:
	jmp __err__
__none__neq__:
	jmp __err__
__none__lt__:
	jmp __err__
__none__le__:
	jmp __err__
__none__gt__:
	jmp __err__
__none__ge__:
	jmp __err__
__none__neg__:
	jmp __err__
__none__not__:
	movq $__bool__True, %rax
	ret
__none__int__:
	movq $16, %rdi
	call __malloc__
	movq $2, 0(%rax)
	movq $0, 8(%rax)
	ret
__none__bool__:
	movq $__bool__False, %rax
	ret
__none__print__:
	movq $__none__print__fmt__, %rdi
	call __printf__
	ret
__bool__add__:
	jmp __err__
__bool__sub__:
	jmp __err__
__bool__mul__:
	jmp __err__
__bool__div__:
	jmp __err__
__bool__mod__:
	jmp __err__
__bool__neg__:
	jmp __err__
__bool__not__:
	pushq %rdi
	movq $16, %rdi
	call __malloc__
	movq $1, 0(%rax)
	popq %rdi
	cmpq $0, 8(%rdi)
	sete %cl
	movzbq %cl, %r10
	movq %r10, 8(%rax)
	ret
__bool__int__:
	pushq %rdi
	movq $16, %rdi
	call __malloc__
	movq $2, 0(%rax)
	popq %rdi
	movq 8(%rdi), %r10
	movq %r10, 8(%rax)
	ret
__bool__bool__:
	movq %rdi, %rax
	ret
__bool__print__:
	cmpq $0, 8(%rdi)
	je __bool__print__neg__
	movq $__bool__True__print__fmt__, %rdi
	jmp __bool__print__nxt__
__bool__print__neg__:
	movq $__bool__False__print__fmt__, %rdi
__bool__print__nxt__:
	call __printf__
	ret
__int__add__:
	pushq %rdi
	movq %rsi, %rdi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *104(%r10)
	movq 8(%rax), %rsi
	popq %rdi
	movq 8(%rdi), %rdi
	addq %rsi, %rdi
	pushq %rdi
	movq $16, %rdi
	call __malloc__
	movq $2, 0(%rax)
	popq %rdi
	movq %rdi, 8(%rax)
	ret
__int__sub__:
	pushq %rdi
	movq %rsi, %rdi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *104(%r10)
	movq 8(%rax), %rsi
	popq %rdi
	movq 8(%rdi), %rdi
	subq %rsi, %rdi
	pushq %rdi
	movq $16, %rdi
	call __malloc__
	movq $2, 0(%rax)
	popq %rdi
	movq %rdi, 8(%rax)
	ret
__int__mul__:
	pushq %rdi
	movq %rsi, %rdi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *104(%r10)
	movq 8(%rax), %rsi
	popq %rdi
	movq 8(%rdi), %rdi
	imulq %rsi, %rdi
	pushq %rdi
	movq $16, %rdi
	call __malloc__
	movq $2, 0(%rax)
	popq %rdi
	movq %rdi, 8(%rax)
	ret
__int__div__:
	pushq %rdi
	movq %rsi, %rdi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *104(%r10)
	movq 8(%rax), %rsi
	popq %rdi
	movq 8(%rdi), %rdi
	movq %rdi, %rax
	idivq %rsi
	movq %rax, %rdi
	pushq %rdi
	movq $16, %rdi
	call __malloc__
	movq $2, 0(%rax)
	popq %rdi
	movq %rdi, 8(%rax)
	ret
__int__mod__:
	pushq %rdi
	movq %rsi, %rdi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *104(%r10)
	movq 8(%rax), %rsi
	popq %rdi
	movq 8(%rdi), %rdi
	movq %rdi, %rax
	idivq %rsi
	movq %rdx, %rdi
	pushq %rdi
	movq $16, %rdi
	call __malloc__
	movq $2, 0(%rax)
	popq %rdi
	movq %rdi, 8(%rax)
	ret
__int__eq__:
	movq 0(%rsi), %r10
	cmpq $2, %r10
	je __int__eq__pos__
	cmpq $1, %r10
	je __int__eq__pos__
	movq $__bool__False, %rax
	ret
__int__eq__pos__:
	pushq %rdi
	movq %rsi, %rdi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *104(%r10)
	movq 8(%rax), %rsi
	popq %rdi
	movq 8(%rdi), %rdi
	cmpq %rsi, %rdi
	sete %cl
	movzbq %cl, %r10
	pushq %r10
	movq $16, %rdi
	call __malloc__
	movq $1, 0(%rax)
	popq %r10
	movq %r10, 8(%rax)
	ret
__int__neq__:
	movq 0(%rsi), %r10
	cmpq $2, %r10
	je __int__neq__pos__
	cmpq $1, %r10
	je __int__neq__pos__
	movq $__bool__True, %rax
	ret
__int__neq__pos__:
	pushq %rdi
	movq %rsi, %rdi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *104(%r10)
	movq 8(%rax), %rsi
	popq %rdi
	movq 8(%rdi), %rdi
	cmpq %rsi, %rdi
	setne %cl
	movzbq %cl, %r10
	pushq %r10
	movq $16, %rdi
	call __malloc__
	movq $1, 0(%rax)
	popq %r10
	movq %r10, 8(%rax)
	ret
__int__lt__:
	pushq %rdi
	movq %rsi, %rdi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *104(%r10)
	movq 8(%rax), %rsi
	popq %rdi
	movq 8(%rdi), %rdi
	cmpq %rsi, %rdi
	setl %cl
	movzbq %cl, %r10
	pushq %r10
	movq $16, %rdi
	call __malloc__
	movq $1, 0(%rax)
	popq %r10
	movq %r10, 8(%rax)
	ret
__int__le__:
	pushq %rdi
	movq %rsi, %rdi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *104(%r10)
	movq 8(%rax), %rsi
	popq %rdi
	movq 8(%rdi), %rdi
	cmpq %rsi, %rdi
	setle %cl
	movzbq %cl, %r10
	pushq %r10
	movq $16, %rdi
	call __malloc__
	movq $1, 0(%rax)
	popq %r10
	movq %r10, 8(%rax)
	ret
__int__gt__:
	pushq %rdi
	movq %rsi, %rdi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *104(%r10)
	movq 8(%rax), %rsi
	popq %rdi
	movq 8(%rdi), %rdi
	cmpq %rsi, %rdi
	setg %cl
	movzbq %cl, %r10
	pushq %r10
	movq $16, %rdi
	call __malloc__
	movq $1, 0(%rax)
	popq %r10
	movq %r10, 8(%rax)
	ret
__int__ge__:
	pushq %rdi
	movq %rsi, %rdi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *104(%r10)
	movq 8(%rax), %rsi
	popq %rdi
	movq 8(%rdi), %rdi
	cmpq %rsi, %rdi
	setge %cl
	movzbq %cl, %r10
	pushq %r10
	movq $16, %rdi
	call __malloc__
	movq $1, 0(%rax)
	popq %r10
	movq %r10, 8(%rax)
	ret
__int__neg__:
	pushq %rdi
	movq $16, %rdi
	call __malloc__
	movq $2, 0(%rax)
	popq %rdi
	movq 8(%rdi), %r10
	negq %r10
	movq %r10, 8(%rax)
	ret
__int__int__:
	movq %rdi, %rax
	ret
__int__bool__:
	pushq %rdi
	movq $16, %rdi
	call __malloc__
	movq $1, 0(%rax)
	popq %rdi
	cmpq $0, 8(%rdi)
	setne %cl
	movzbq %cl, %r10
	movq %r10, 8(%rax)
	ret
__int__print__:
	movq 8(%rdi), %rsi
	movq $__int__print__fmt__, %rdi
	call __printf__
	ret
__string__add__:
	movq 0(%rsi), %r10
	cmpq $3, %r10
	je t_1
	jmp __err__
t_1:
	pushq %r12
	pushq %r13
	pushq %r14
	movq %rdi, %r12
	movq %rsi, %r13
	movq 8(%r12), %rdi
	addq 8(%r13), %rdi
	movq %rdi, %r14
	addq $17, %rdi
	call __malloc__
	movq $3, 0(%rax)
	movq %r14, 8(%rax)
	movq %rax, %r14
	leaq 16(%r14), %rdi
	leaq 16(%r12), %rsi
	call __strcpy__
	leaq 16(%r14), %rdi
	leaq 16(%r13), %rsi
	call __strcat__
	movq %r14, %rax
	popq %r14
	popq %r13
	popq %r12
	ret
__string__sub__:
	jmp __err__
__string__mul__:
	jmp __err__
__string__div__:
	jmp __err__
__string__mod__:
	jmp __err__
__string__eq__:
	movq 0(%rsi), %r10
	cmpq $3, %r10
	je __string__eq__pos__
	movq $__bool__False, %rax
	ret
__string__eq__pos__:
	addq $16, %rdi
	addq $16, %rsi
	call __strcmp__
	setz %cl
	pushq %rcx
	movq $16, %rdi
	call __malloc__
	movq $1, 0(%rax)
	popq %rcx
	movzbq %cl, %r10
	movq %r10, 8(%rax)
	ret
__string__neq__:
	movq 0(%rsi), %r10
	cmpq $3, %r10
	je __string__neq__pos__
	movq $__bool__True, %rax
	ret
__string__neq__pos__:
	addq $16, %rdi
	addq $16, %rsi
	call __strcmp__
	setnz %cl
	pushq %rcx
	movq $16, %rdi
	call __malloc__
	movq $1, 0(%rax)
	popq %rcx
	movzbq %cl, %r10
	movq %r10, 8(%rax)
	ret
__string__lt__:
	movq 0(%rsi), %r10
	cmpq $3, %r10
	je t_2
	jmp __err__
t_2:
	addq $16, %rdi
	addq $16, %rsi
	call __strcmp__
	setl %cl
	pushq %rcx
	movq $16, %rdi
	call __malloc__
	movq $1, 0(%rax)
	popq %rcx
	movzbq %cl, %r10
	movq %r10, 8(%rax)
	ret
__string__le__:
	movq 0(%rsi), %r10
	cmpq $3, %r10
	je t_3
	jmp __err__
t_3:
	addq $16, %rdi
	addq $16, %rsi
	call __strcmp__
	setle %cl
	pushq %rcx
	movq $16, %rdi
	call __malloc__
	movq $1, 0(%rax)
	popq %rcx
	movzbq %cl, %r10
	movq %r10, 8(%rax)
	ret
__string__gt__:
	movq 0(%rsi), %r10
	cmpq $3, %r10
	je t_4
	jmp __err__
t_4:
	addq $16, %rdi
	addq $16, %rsi
	call __strcmp__
	setg %cl
	pushq %rcx
	movq $16, %rdi
	call __malloc__
	movq $1, 0(%rax)
	popq %rcx
	movzbq %cl, %r10
	movq %r10, 8(%rax)
	ret
__string__ge__:
	movq 0(%rsi), %r10
	cmpq $3, %r10
	je t_5
	jmp __err__
t_5:
	addq $16, %rdi
	addq $16, %rsi
	call __strcmp__
	setge %cl
	pushq %rcx
	movq $16, %rdi
	call __malloc__
	movq $1, 0(%rax)
	popq %rcx
	movzbq %cl, %r10
	movq %r10, 8(%rax)
	ret
__string__neg__:
	jmp __err__
__string__int__:
	jmp __err__
__string__print__:
	addq $16, %rdi
	call __printf__
	ret
__list__add__:
	movq 0(%rsi), %r10
	cmpq $4, %r10
	je t_6
	jmp __err__
t_6:
	movq %rdi, %r8
	movq 8(%rdi), %rdi
	addq 8(%rsi), %rdi
	movq %rdi, %r9
	leaq 16(, %rdi, 8), %rdi
	pushq %r8
	pushq %rsi
	pushq %r9
	call __malloc__
	popq %r9
	popq %rsi
	popq %r8
	movq $4, 0(%rax)
	movq %r9, 8(%rax)
	leaq 16(%rax), %r9
	leaq 16(%r8), %rcx
	movq 8(%r8), %rdx
	leaq 16(%r8, %rdx, 8), %rdx
__list__add__1__loop__:
	cmpq %rcx, %rdx
	je __list__add__1__end__
	movq (%rcx), %r10
	movq %r10, (%r9)
	addq $8, %r9
	addq $8, %rcx
	jmp __list__add__1__loop__
__list__add__1__end__:
	leaq 16(%rsi), %rcx
	movq 8(%rsi), %rdx
	leaq 16(%rsi, %rdx, 8), %rdx
__list__add__2__loop__:
	cmpq %rcx, %rdx
	je __list__add__2__end__
	movq (%rcx), %r10
	movq %r10, (%r9)
	addq $8, %r9
	addq $8, %rcx
	jmp __list__add__2__loop__
__list__add__2__end__:
	ret
__list__sub__:
	jmp __err__
__list__mul__:
	jmp __err__
__list__div__:
	jmp __err__
__list__mod__:
	jmp __err__
__list__eq__:
	movq 0(%rsi), %r10
	cmpq $4, %r10
	jne __list__eq__neg__
	movq 8(%rsi), %r10
	cmpq %r10, 8(%rdi)
	jne __list__eq__neg__
	pushq %rbx
	pushq %r12
	pushq %r13
	pushq %r14
	leaq 16(%rsi), %r13
	movq $__bool__True, %rbx
	leaq 16(%rdi), %r12
	movq 8(%rdi), %r14
	leaq 16(%rdi, %r14, 8), %r14
__list__eq__loop__:
	cmpq %r12, %r14
	je __list__eq__end__
	movq (%r12), %rdi
	movq (%r13), %rsi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *40(%r10)
	cmpq $1, 8(%rax)
	je __list__eq__ctn__
	movq $__bool__False, %rbx
	jmp __list__eq__brk__
__list__eq__ctn__:
	addq $8, %r13
	addq $8, %r12
	jmp __list__eq__loop__
__list__eq__end__:
__list__eq__brk__:
	movq %rbx, %rax
	popq %r14
	popq %r13
	popq %r12
	popq %rbx
	ret
__list__eq__neg__:
	movq $__bool__False, %rax
	ret
__list__neq__:
	call __list__eq__
	cmpq $0, 8(%rax)
	je __list__neq__pos
	movq $__bool__False, %rax
	ret
__list__neq__pos:
	movq $__bool__True, %rax
	ret
__list__lt__:
	movq 0(%rsi), %r10
	cmpq $4, %r10
	je t_7
	jmp __err__
t_7:
	movq 0(%rsi), %r10
	cmpq $4, %r10
	jne __list__eq__neg__
	call __comp__length__list__
	pushq %rbx
	pushq %r12
	pushq %r13
	pushq %r14
	pushq %rdx
	leaq 16(%rcx), %r13
	movq $__bool__True, %rbx
	leaq 16(%rax), %r12
	movq 8(%rax), %r14
	leaq 16(%rax, %r14, 8), %r14
__list__eq_lt___loop__:
	cmpq %r12, %r14
	je __list__eq_lt___end__
	movq (%r12), %rdi
	movq (%r13), %rsi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *56(%r10)
	cmpq $1, 8(%rax)
	je __list__lt__ctn__
	movq $__bool__False, %rbx
	jmp __list__lt__brk__
__list__lt__ctn__:
	addq $8, %r13
	addq $8, %r12
	jmp __list__eq_lt___loop__
__list__eq_lt___end__:
__list__lt__brk__:
	movq %rbx, %rax
	popq %rdx
	popq %r14
	popq %r13
	popq %r12
	popq %rbx
	ret
__list__lt__neg__:
	movq $__bool__False, %rax
	ret
__list__le__:
	movq 0(%rsi), %r10
	cmpq $4, %r10
	je t_8
	jmp __err__
t_8:
	movq 0(%rsi), %r10
	cmpq $4, %r10
	jne __list__eq__neg__
	call __comp__length__list__
	pushq %rbx
	pushq %r12
	pushq %r13
	pushq %r14
	pushq %rdx
	leaq 16(%rcx), %r13
	movq $__bool__True, %rbx
	leaq 16(%rax), %r12
	movq 8(%rax), %r14
	leaq 16(%rax, %r14, 8), %r14
__list__eq_le___loop__:
	cmpq %r12, %r14
	je __list__eq_le___end__
	movq (%r12), %rdi
	movq (%r13), %rsi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *64(%r10)
	cmpq $1, 8(%rax)
	je __list__le__ctn__
	movq $__bool__False, %rbx
	jmp __list__le__brk__
__list__le__ctn__:
	addq $8, %r13
	addq $8, %r12
	jmp __list__eq_le___loop__
__list__eq_le___end__:
__list__le__brk__:
	movq %rbx, %rax
	popq %rdx
	popq %r14
	popq %r13
	popq %r12
	popq %rbx
	ret
__list__le__neg__:
	movq $__bool__False, %rax
	ret
__list__gt__:
	movq 0(%rsi), %r10
	cmpq $4, %r10
	je t_9
	jmp __err__
t_9:
	movq 0(%rsi), %r10
	cmpq $4, %r10
	jne __list__eq__neg__
	call __comp__length__list__
	pushq %rbx
	pushq %r12
	pushq %r13
	pushq %r14
	pushq %rdx
	leaq 16(%rcx), %r13
	movq $__bool__True, %rbx
	leaq 16(%rax), %r12
	movq 8(%rax), %r14
	leaq 16(%rax, %r14, 8), %r14
__list__eq_gt___loop__:
	cmpq %r12, %r14
	je __list__eq_gt___end__
	movq (%r12), %rdi
	movq (%r13), %rsi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *72(%r10)
	cmpq $1, 8(%rax)
	je __list__gt__ctn__
	movq $__bool__False, %rbx
	jmp __list__gt__brk__
__list__gt__ctn__:
	addq $8, %r13
	addq $8, %r12
	jmp __list__eq_gt___loop__
__list__eq_gt___end__:
__list__gt__brk__:
	movq %rbx, %rax
	popq %rdx
	popq %r14
	popq %r13
	popq %r12
	popq %rbx
	ret
__list__gt__neg__:
	movq $__bool__False, %rax
	ret
__list__ge__:
	movq 0(%rsi), %r10
	cmpq $4, %r10
	je t_10
	jmp __err__
t_10:
	movq 0(%rsi), %r10
	cmpq $4, %r10
	jne __list__eq__neg__
	call __comp__length__list__
	pushq %rbx
	pushq %r12
	pushq %r13
	pushq %r14
	pushq %rdx
	leaq 16(%rcx), %r13
	movq $__bool__True, %rbx
	leaq 16(%rax), %r12
	movq 8(%rax), %r14
	leaq 16(%rax, %r14, 8), %r14
__list__eq_ge___loop__:
	cmpq %r12, %r14
	je __list__eq_ge___end__
	movq (%r12), %rdi
	movq (%r13), %rsi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *80(%r10)
	cmpq $1, 8(%rax)
	je __list__ge__ctn__
	movq $__bool__False, %rbx
	jmp __list__ge__brk__
__list__ge__ctn__:
	addq $8, %r13
	addq $8, %r12
	jmp __list__eq_ge___loop__
__list__eq_ge___end__:
__list__ge__brk__:
	movq %rbx, %rax
	popq %rdx
	popq %r14
	popq %r13
	popq %r12
	popq %rbx
	ret
__list__ge__neg__:
	movq $__bool__False, %rax
	ret
__list__neg__:
	jmp __err__
__list__int__:
	jmp __err__
__list__print__:
	pushq %r12
	pushq %r13
	pushq %r14
	xorq %r12, %r12
	movq 8(%rdi), %r13
	movq %rdi, %r14
	movq $__list__print__bko__, %rdi
	call __printf__
__list__print__lop__:
	cmpq %r12, %r13
	je __list__print__end__
	cmpq $0, %r12
	je __list__print__sks__
	movq $__list__print__sep__, %rdi
	call __printf__
__list__print__sks__:
	leaq 16(%r14, %r12, 8), %rdi
	movq (%rdi), %rdi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *120(%r10)
	incq %r12
	jmp __list__print__lop__
__list__print__end__:
	movq $__list__print__bkc__, %rdi
	call __printf__
	popq %r14
	popq %r13
	popq %r12
	ret
__main__:
	movq $c_0, %rax
	movq %rax, %rdi
	movq 0(%rdi), %r10
	leaq (%r15, %r10, 8), %r10
	movq (%r10), %r10
	call *120(%r10)
	movq $__print__lnbrk__, %rdi
	call __printf__
	xorq %rax, %rax
	ret
	.data
__none__None__:
	.quad 0
	.quad 0
__none__print__fmt__:
	.string "None"
__bool__True:
	.quad 1
	.quad 1
__bool__False:
	.quad 1
	.quad 0
__bool__True__print__fmt__:
	.string "True"
__bool__False__print__fmt__:
	.string "False"
__int__print__fmt__:
	.string "%d"
__int__zero__:
	.quad 2
	.quad 0
__print__lnbrk__:
	.string "\n"
__list__print__bko__:
	.string "["
__list__print__sep__:
	.string ", "
__list__print__bkc__:
	.string "]"
c_0:
	.quad 3
	.quad 12
	.string "Hello World!"
