package com.walmart.cditest;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.AnnotationLiteral;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class CDIGetInstanceTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Deployment
  public static Archive<?> deploy() {

    return ShrinkWrap.create(JavaArchive.class)
        .addClasses(
            TestQualifier.class,
            QualifiedBean.class)
        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  /*
   * Tests that I find a qualified bean if I select with the correct qualifier.
   */
  @Test
  public void shouldSelectInstanceWithQualifier() {
    final Instance<QualifiedBean> instance = CDI.current().select(QualifiedBean.class, new AnnotationLiteral<TestQualifier>() {});
    QualifiedBean bean = instance.get();
    assertEquals(42, bean.f());
  }

  /*
   * Tests that I find a qualified bean if I select without any identifier, therefore searching all beans.
   */
  @Test
  public void shouldNotFindUnqualifiedInstance() {
    final Instance<QualifiedBean> instance = CDI.current().select(QualifiedBean.class);
    QualifiedBean bean = instance.get();
    assertEquals(42, bean.f());
  }

}
