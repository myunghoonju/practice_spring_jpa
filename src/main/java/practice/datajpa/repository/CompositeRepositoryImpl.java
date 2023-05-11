package practice.datajpa.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import practice.datajpa.entity.composite.CompositeDto;

import javax.persistence.EntityManager;

import static practice.datajpa.entity.composite.QComposite.composite;

public class CompositeRepositoryImpl implements CompositeRepositoryCustom {

    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    public CompositeRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public String findUniqueAuthor(String author) {
        CompositeDto c = queryFactory.selectDistinct(getColumn())
                                     .from(composite)
                                     .where(composite.id.author.eq(author))
                                     .fetchFirst();

        return c.getAuthor();
    }

    private QBean<CompositeDto> getColumn() {
        return Projections.fields(
                CompositeDto.class,
                composite.id.author);
    }
}
